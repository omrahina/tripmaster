package com.tripmaster.location.service;

import com.tripmaster.location.dto.LocationHistoryDto;
import com.tripmaster.location.exceptions.UserLocationException;
import com.tripmaster.location.model.UserLocation;
import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TrackerService {

    private final Logger LOGGER = LoggerFactory.getLogger(TrackerService.class);

    private final GpsUtil gpsUtil;
    private final ExecutorService trackerExecutorService;

    public TrackerService(GpsUtil gpsUtil, @Qualifier("fixedTrackerThreadPool") ExecutorService trackerExecutorService) {
        this.gpsUtil = gpsUtil;
        this.trackerExecutorService = trackerExecutorService;
    }

    public Future<VisitedLocation> trackUserLocation(UserLocation userLocation){
        return trackerExecutorService.submit(() -> {
            VisitedLocation visitedLocation = gpsUtil.getUserLocation(userLocation.getUserId());
            userLocation.addToVisitedLocations(visitedLocation);
            return visitedLocation;
        });
    }

    public VisitedLocation getUserLocation(UserLocation userLocation) throws UserLocationException{
        if (userLocation.getVisitedLocations().isEmpty()) {
            try {
                LOGGER.info("Get User Location");
                return trackUserLocation(userLocation).get();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("User Location Failure");
                LOGGER.debug(e.getMessage());
                throw new UserLocationException("User Location Failure");
            }
        }

        return userLocation.getLastVisitedLocation();
    }

    public List<LocationHistoryDto> getAllKnownLocations(List<UserLocation> userLocations) {
        List<LocationHistoryDto> locationHistoryList = userLocations.stream()
                .filter(user -> !user.getVisitedLocations().isEmpty())
                .map(user -> {
                    List<Location> locations = user.getVisitedLocations().stream()
                            .map(visitedLocation -> visitedLocation.location)
                            .collect(Collectors.toList());
                    return new LocationHistoryDto(user.getUserId(), locations);
                })
                .collect(Collectors.toList());
        if (!locationHistoryList.isEmpty()) {
            LOGGER.info("Visited location(s) retrieved");
            return locationHistoryList;
        }
        LOGGER.error("No visited location found");
        return null;
    }

    /**
     * Assures that no new task is submitted and awaits for executing tasks to terminate
     */
    public void stopTracking() {
        trackerExecutorService.shutdown();
        try {
            trackerExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
        }
    }

}
