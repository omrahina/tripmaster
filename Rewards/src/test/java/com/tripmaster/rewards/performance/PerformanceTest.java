package com.tripmaster.rewards.performance;

import com.tripmaster.rewards.data.MockRewardDataUtils;
import com.tripmaster.rewards.dto.LocationDto;
import com.tripmaster.rewards.dto.VisitedLocationDto;
import com.tripmaster.rewards.model.UserReward;
import com.tripmaster.rewards.service.RewardService;
import com.tripmaster.rewards.service.UserRewardService;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rewardCentral.RewardCentral;
import tripPricer.TripPricer;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceTest {

    private GpsUtil gpsUtil;
    private RewardService rewardService;
    private UserRewardService userRewardService;
    private TripPricer tripPricer;
    private final ExecutorService rewardsExecutorService = Executors.newFixedThreadPool(45);

    @BeforeEach
    public void setUp(){
        gpsUtil = new GpsUtil();
        tripPricer = new TripPricer();
        rewardService = new RewardService(gpsUtil, new RewardCentral(), rewardsExecutorService, tripPricer);
        userRewardService = new UserRewardService();
    }

    @Test
    public void highVolumeGetRewards() {
        MockRewardDataUtils.setInternalUserNumber(10);
        MockRewardDataUtils.initializeInternalRewards();

        Attraction attraction = gpsUtil.getAttractions().get(0);
        List<UserReward> allUserRewardEntries = userRewardService.getAllUserRewardEntries();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        allUserRewardEntries.forEach(entry ->
                rewardService.calculateRewards(entry,
                        Collections.singletonList(new VisitedLocationDto(entry.getId(), new LocationDto(attraction.longitude, attraction.latitude), new Date()))
                )
        );
        rewardService.stopRewarding();
        stopWatch.stop();

        for (UserReward entry : allUserRewardEntries) {
            assertTrue(entry.getRewards().size() > 0);
        }
        System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }

}
