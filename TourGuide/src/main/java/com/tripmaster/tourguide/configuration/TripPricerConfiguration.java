package com.tripmaster.tourguide.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tripPricer.TripPricer;

@Configuration
public class TripPricerConfiguration {

    @Bean
    public TripPricer getTripPricer() {
        return new TripPricer();
    }
}
