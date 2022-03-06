package com.tripmaster.rewards.service;

import com.tripmaster.rewards.dto.LocationDto;
import com.tripmaster.rewards.dto.NearbyAttractionDto;
import com.tripmaster.rewards.dto.VisitedLocationDto;
import com.tripmaster.rewards.exceptions.NoDealOrRewardException;
import com.tripmaster.rewards.model.Reward;
import com.tripmaster.rewards.model.UserReward;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import rewardCentral.RewardCentral;
import tripPricer.TripPricer;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RewardsServiceTest {

    @Mock
    private GpsUtil gpsUtil;

    @Mock
    private RewardCentral rewardsCentral;

    @InjectMocks
    private RewardService rewardService;

    private TripPricer tripPricer;

    private Attraction attraction;

    @BeforeEach
    public void setUp(){
        ExecutorService rewardsExecutorService = Executors.newFixedThreadPool(45);
        tripPricer = new TripPricer();
        rewardService = new RewardService(gpsUtil, rewardsCentral, rewardsExecutorService, tripPricer);
        attraction = new Attraction("Disneyland", "Anaheim", "CA", 33.817595D, -117.922008D);
    }

    @Test
    public void getUserRewards_should_return_userRewards() throws ExecutionException, InterruptedException {
        when(gpsUtil.getAttractions()).thenReturn(Collections.singletonList(attraction));
        when(rewardsCentral.getAttractionRewardPoints(any(), any())).thenReturn(5);
        UserReward userReward = new UserReward(UUID.randomUUID(), "jon");
        VisitedLocationDto visitedLocation = new VisitedLocationDto(userReward.getId(), new LocationDto(attraction.longitude, attraction.latitude), new Date());

        List<Reward> rewards = rewardService.getUserRewards(userReward, Collections.singletonList(visitedLocation));
        rewardService.stopRewarding();

        assertTrue(userReward.getRewards().size() > 0);
    }

    @Test
    public void getUserRewards_should_throw_NoDealOrRewardException() {
        when(gpsUtil.getAttractions()).thenReturn(Collections.singletonList(attraction));
        UserReward userReward = new UserReward(UUID.randomUUID(), "jon");

        assertThatExceptionOfType(NoDealOrRewardException.class).isThrownBy(()
                -> rewardService.getUserRewards(userReward, new ArrayList<>()))
                .withMessage("Rewards record empty");
    }

    @Test
    public void getNearByAttractions_should_return_nearByAttractions() {
        when(gpsUtil.getAttractions()).thenReturn(Collections.singletonList(attraction));
        when(rewardsCentral.getAttractionRewardPoints(any(), any())).thenReturn(5);
        VisitedLocationDto visitedLocation = new VisitedLocationDto(UUID.randomUUID(), new LocationDto(attraction.longitude, attraction.latitude), new Date());

        NearbyAttractionDto nearbyAttractions = rewardService.getNearByAttractions(visitedLocation);

        assertEquals(visitedLocation.getLocation(), nearbyAttractions.getUserLocation());
        assertThat(nearbyAttractions.getNearbyAttractions()).isNotEmpty().hasSize(1);
    }

    @Test
    public void getNearbyAttractions_nothing_found() {
        when(gpsUtil.getAttractions()).thenReturn(new ArrayList<>());
        VisitedLocationDto visitedLocation = new VisitedLocationDto(UUID.randomUUID(), new LocationDto(attraction.longitude, attraction.latitude), new Date());

        NearbyAttractionDto nearbyAttractions = rewardService.getNearByAttractions(visitedLocation);

        assertNull(nearbyAttractions.getNearbyAttractions());

    }

    @Test
    public void should_calculate_distance_between_two_locations() {
        VisitedLocationDto visitedLocation = new VisitedLocationDto(UUID.randomUUID(), new LocationDto(attraction.longitude, attraction.latitude), new Date());

        assertTrue(rewardService.getDistance(attraction, visitedLocation.getLocation()) < 1);

    }

}
