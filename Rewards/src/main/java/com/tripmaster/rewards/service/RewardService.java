package com.tripmaster.rewards.service;

import com.tripmaster.rewards.dto.LocationDto;
import com.tripmaster.rewards.dto.UserPreferencesDto;
import com.tripmaster.rewards.dto.VisitedLocationDto;
import com.tripmaster.rewards.exceptions.NoRewardException;
import com.tripmaster.rewards.model.Reward;
import com.tripmaster.rewards.model.UserPreferences;
import com.tripmaster.rewards.model.UserReward;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class RewardService {

    private final Logger LOGGER = LoggerFactory.getLogger(RewardService.class);

    private final GpsUtil gpsUtil;
    private final RewardCentral rewardsCentral;
    private final ExecutorService rewardsExecutorService;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    private final int ATTRACTION_PROXIMITY_RANGE = 200;
    private int proximityBuffer;

    public RewardService(GpsUtil gpsUtil, RewardCentral rewardCentral, @Qualifier("fixedRewardsThreadPool") ExecutorService rewardsExecutorService) {
        this.gpsUtil = gpsUtil;
        this.rewardsCentral = rewardCentral;
        this.rewardsExecutorService = rewardsExecutorService;
        this.proximityBuffer = 10;
    }

    public int getProximityBuffer() {
        return proximityBuffer;
    }

    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    public List<Reward> getUserRewards(UserReward userReward , List<VisitedLocationDto> visitedLocations) throws ExecutionException, InterruptedException, NoRewardException {
        List<Reward> rewards = calculateRewards(userReward, visitedLocations).get();
        if (!rewards.isEmpty()){
            LOGGER.info("user rewards retrieved");
            return rewards;
        }
        LOGGER.debug("No rewards found");
        throw new NoRewardException("Rewards record empty");
    }

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

    public UserReward updatePreferences(UserReward userReward, UserPreferencesDto userPreferences) {
        UserPreferences newPreferences = new UserPreferences(userPreferences.getTripDuration(), userPreferences.getTicketQuantity(),
                userPreferences.getNumberOfAdults(), userPreferences.getNumberOfChildren());
        userReward.setUserPreferences(newPreferences);
        return userReward;
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

    private boolean nearAttraction(VisitedLocationDto visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.getLocation()) > proximityBuffer ? false : true;
    }

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

    private int getRewardPoints(Attraction attraction, UserReward userReward) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, userReward.getId());
    }

}
