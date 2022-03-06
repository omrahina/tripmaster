package com.tripmaster.location.performance;

import com.tripmaster.location.data.MockLocationDataUtils;
import com.tripmaster.location.model.UserLocation;
import com.tripmaster.location.service.TrackerService;
import com.tripmaster.location.service.UserLocationService;
import gpsUtil.GpsUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PerformanceTest {

    private GpsUtil gpsUtil;
    private TrackerService trackerService;
    private UserLocationService userLocationService;
    private final ExecutorService trackerExecutorService = Executors.newFixedThreadPool(9);

    @BeforeEach
    public void setUp(){
        gpsUtil = new GpsUtil();
        trackerService = new TrackerService(gpsUtil, trackerExecutorService);
        userLocationService = new UserLocationService();
        Locale.setDefault(new Locale("en", "US"));
    }

    @Test
    public void highVolumeTrackLocation() {
        MockLocationDataUtils.setInternalUserNumber(10);
        MockLocationDataUtils.initializeInternalLocations();

        List<UserLocation> allUserLocations = userLocationService.getAllUserLocations();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (UserLocation location : allUserLocations) {
            trackerService.trackUserLocation(location);
        }
        trackerService.stopTracking();
        stopWatch.stop();

        System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
        assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
    }


}
