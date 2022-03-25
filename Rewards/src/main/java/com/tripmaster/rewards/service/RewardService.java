package com.tripmaster.rewards.service;

import com.tripmaster.rewards.dto.*;
import com.tripmaster.rewards.exceptions.NoDealOrRewardException;
import com.tripmaster.rewards.model.Reward;
import com.tripmaster.rewards.model.UserPreferences;
import com.tripmaster.rewards.model.UserReward;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RewardService {

    @Value("${tripPricer.tripPricerApiKey}")
    private String tripPricerApiKey;

    private final Logger LOGGER = LoggerFactory.getLogger(RewardService.class);

    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;
    private final ExecutorService rewardsExecutorService;
    private final TripPricer tripPricer;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private int proximityBuffer;

    public RewardService(GpsUtil gpsUtil, RewardCentral rewardCentral, @Qualifier("fixedRewardsThreadPool") ExecutorService rewardsExecutorService, TripPricer tripPricer) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
        this.rewardsExecutorService = rewardsExecutorService;
        this.tripPricer = tripPricer;
        this.proximityBuffer = 10;
    }

    /**
     * Returns a user's reward points
     * @param userReward the reward entry
     * @param visitedLocations a list of visited locations
     * @return a list of reward
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NoDealOrRewardException
     */
    public List<Reward> getUserRewards(UserReward userReward , List<VisitedLocationDto> visitedLocations) throws ExecutionException, InterruptedException, NoDealOrRewardException {
        List<Reward> rewards = calculateRewards(userReward, visitedLocations).get();
        if (!rewards.isEmpty()){
            LOGGER.info("user rewards retrieved");
            return rewards;
        }
        LOGGER.debug("No rewards found");
        throw new NoDealOrRewardException("Rewards record empty");
    }

    /**
     * Calculates the reward points
     * @param userReward the reward entry
     * @param userLocations a list of visited locations
     * @return a future list of rewards
     */
    public Future<List<Reward>> calculateRewards(UserReward userReward , List<VisitedLocationDto> userLocations) {
        return rewardsExecutorService.submit(() -> {
            List<Attraction> attractions = gpsUtil.getAttractions();

            for(VisitedLocationDto visitedLocation : userLocations) {
                for(Attraction attraction : attractions) {
                    if(userReward.getRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
                        if(nearAttraction(visitedLocation, attraction)) {
                            userReward.addReward(new Reward(visitedLocation, attraction, getRewardPoints(attraction, userReward)));
                        }
                    }
                }
            }
            return userReward.getRewards();
        });
    }

    /**
     * Updates user preferences
     * @param userReward the reward entry
     * @param userPreferences the preferences
     * @return the updated reward entry
     */
    public UserReward updatePreferences(UserReward userReward, UserPreferencesDto userPreferences) {
        UserPreferences newPreferences = new UserPreferences(userPreferences.getTripDuration(), userPreferences.getTicketQuantity(),
                userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren());
        userReward.setUserPreferences(newPreferences);
        return userReward;
    }

    /**
     * Returns the five nearest attractions from a specified position
     * @param visitedLocation the last known position
     * @return the attractions
     */
    public NearbyAttractionDto getNearByAttractions(VisitedLocationDto visitedLocation) {
        LocationDto userLocation = visitedLocation.getLocation();
        List<Attraction> fiveNearestAttractions = gpsUtil.getAttractions().stream()
                .sorted(Comparator.comparingDouble(attraction -> getDistance(attraction, userLocation)))
                .limit(5)
                .collect(Collectors.toList());
        if (!fiveNearestAttractions.isEmpty()) {
            List<AttractionInfo> attractions = fiveNearestAttractions.stream()
                    .map(attraction -> new AttractionInfo(attraction, getDistance(attraction, userLocation),
                            rewardsCentral.getAttractionRewardPoints(attraction.attractionId, visitedLocation.getUserId())))
                    .collect(Collectors.toList());
            LOGGER.info(fiveNearestAttractions.size() +" nearest attraction(s) retrieved");
            return new NearbyAttractionDto(userLocation, attractions);
        }
        LOGGER.error("Cannot return the nearest attractions");
        return new NearbyAttractionDto(userLocation);
    }

    /**
     * Get trip deals according to user's preferences
     * @param userReward
     * @return the corresponding deals
     * @throws NoDealOrRewardException
     */
    public List<Provider> getTripDeals(UserReward userReward) throws NoDealOrRewardException{
        int cumulativeRewardPoints = userReward.getRewards().stream().mapToInt(Reward::getRewardPoints).sum();
        List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, userReward.getId(), userReward.getUserPreferences().getNumberOfAdults(),
                userReward.getUserPreferences().getNumberOfChildren(), userReward.getUserPreferences().getTripDuration(), cumulativeRewardPoints);
        if (!providers.isEmpty()) {
            LOGGER.info("TripDeals retrieved");
            userReward.setTripDeals(providers);
            return providers;
        }
        LOGGER.debug("Trip deals empty");
        throw new NoDealOrRewardException("No Trip Deal available");
    }

    /**
     * Assures that no new task is submitted and awaits for executing tasks to terminate
     */
    public void stopRewarding() {
        rewardsExecutorService.shutdown();
        try {
            rewardsExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Calculates the distance between two positions
     * @param loc1
     * @param loc2
     * @return the distance
     */
    public double getDistance(Location loc1, LocationDto loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.getLatitude());
        double lon2 = Math.toRadians(loc2.getLongitude());

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
    }

    private boolean nearAttraction(VisitedLocationDto visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.getLocation()) > proximityBuffer ? false : true;
    }

    private int getRewardPoints(Attraction attraction, UserReward userReward) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, userReward.getId());
    }

}
