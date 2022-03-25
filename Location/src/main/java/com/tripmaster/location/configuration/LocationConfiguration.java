package com.tripmaster.location.configuration;

import gpsUtil.GpsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class LocationConfiguration {

    @Value("${tracker.nbThreads}")
    private int numberOfThreads;

    @Bean("fixedTrackerThreadPool")
    public ExecutorService fixedTrackerThreadPool() {
        return Executors.newFixedThreadPool(numberOfThreads);
    }

    @Bean
    public GpsUtil getGpsUtil() {
        return new GpsUtil();
    }

}
