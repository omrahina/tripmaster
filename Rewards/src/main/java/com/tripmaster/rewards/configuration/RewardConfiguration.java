package com.tripmaster.rewards.configuration;

import gpsUtil.GpsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;
import tripPricer.TripPricer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class RewardConfiguration {

    @Value("${rewards.nbThreads}")
    private int numberOfThreads;

    @Bean("fixedRewardsThreadPool")
    public ExecutorService fixedRewardsThreadPool() {
        return Executors.newFixedThreadPool(numberOfThreads);
    }

    @Bean
    public GpsUtil getGpsUtil() {
        return new GpsUtil();
    }

    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }

    @Bean
    public TripPricer getTripPricer() {
        return new TripPricer();
    }

}
