package com.tripmaster.location.service;

import com.tripmaster.location.data.MockLocationDataUtils;
import com.tripmaster.location.dto.LocationHistoryDto;
import com.tripmaster.location.model.UserLocation;
import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TrackerServiceTest {

    @Mock
    private GpsUtil gpsUtil;

    @InjectMocks
    private TrackerService trackerService;

    private UUID userId;

    @BeforeAll
    public static void setUp() {
        Locale.setDefault(new Locale("en", "US"));
    }

    @BeforeEach
    public void setUpEachTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(9);
        trackerService = new TrackerService(gpsUtil, executorService);
        userId = UUID.randomUUID();
    }

    @Test
    public void should_trackUserLocation_ok() {
        when(gpsUtil.getUserLocation(any())).thenReturn(new VisitedLocation(userId, new Location(0.12, 0.3), new Date()));
        UserLocation userLocation = new UserLocation(userId, "jon");
        trackerService.trackUserLocation(userLocation);
        trackerService.stopTracking();

        assertEquals(1, userLocation.getVisitedLocations().size());
    }

    @Test
    public void getUserLocation_empty_visitedLocation_case() {
        when(gpsUtil.getUserLocation(any())).thenReturn(new VisitedLocation(userId, new Location(0.12, 0.3), new Date()));
        UserLocation userLocation = new UserLocation(userId, "jon");
        VisitedLocation visitedLocation = trackerService.getUserLocation(userLocation);
        trackerService.stopTracking();

        assertEquals(visitedLocation.userId, userLocation.getUserId());
    }

    @Test
    public void getUserLocation_not_empty_visitedLocations_case() {
        UserLocation userLocation = new UserLocation(userId, "jon");
        VisitedLocation visitedLocation = new VisitedLocation(userId, new Location(0.12, 0.3), new Date());
        userLocation.addToVisitedLocations(visitedLocation);
        VisitedLocation visited = trackerService.getUserLocation(userLocation);

        assertEquals(visitedLocation, visited);
    }

    @Test
    public void should_return_all_known_locations() {
        MockLocationDataUtils.setInternalUserNumber(2);
        MockLocationDataUtils.initializeInternalLocations();
        MockLocationDataUtils.getInternalUserLocationMap().get("internalUser0").getVisitedLocations().clear();
        List<UserLocation> userLocations = new ArrayList<>(MockLocationDataUtils.getInternalUserLocationMap().values());

        List<LocationHistoryDto> locationHistory = trackerService.getAllKnownLocations(userLocations);

        assertNotNull(locationHistory);
        assertEquals(1, locationHistory.size());
    }

    @Test
    public void should_not_find_any_location_history() {
        MockLocationDataUtils.setInternalUserNumber(2);
        MockLocationDataUtils.initializeInternalLocations();
        MockLocationDataUtils.getInternalUserLocationMap().get("internalUser0").getVisitedLocations().clear();
        MockLocationDataUtils.getInternalUserLocationMap().get("internalUser1").getVisitedLocations().clear();
        List<UserLocation> userLocations = new ArrayList<>(MockLocationDataUtils.getInternalUserLocationMap().values());

        List<LocationHistoryDto> locationHistory = trackerService.getAllKnownLocations(userLocations);

        assertNull(locationHistory);
    }

}
