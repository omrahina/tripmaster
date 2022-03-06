package com.tripmaster.location.data;

import com.tripmaster.location.model.UserLocation;
import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.IntStream;

public class MockLocationDataUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockLocationDataUtils.class);
    private static final Map<String, UserLocation> INTERNAL_USER_LOCATION_MAP = new HashMap<>();
    private static int internalUserNumber = 10;

    static {
        initializeInternalLocations();
    }

    public static Map<String, UserLocation> getInternalUserLocationMap() {
        return INTERNAL_USER_LOCATION_MAP;
    }

    public static void addLocationEntry(UserLocation userLocation) {
        INTERNAL_USER_LOCATION_MAP.put(userLocation.getUsername(), userLocation);
    }

    public static int getInternalUserNumber() {
        return internalUserNumber;
    }

    public static void setInternalUserNumber(int internalUserNumber) {
        MockLocationDataUtils.internalUserNumber = internalUserNumber;
    }

    public static void initializeInternalLocations() {
        LOGGER.info("Initializing locations");
        INTERNAL_USER_LOCATION_MAP.clear();
        IntStream.range(0, MockLocationDataUtils.getInternalUserNumber()).forEach(i -> {
            String username = "internalUser" + i;
            UserLocation userLocation = new UserLocation(UUID.randomUUID(), username);
            generateUserLocationHistory(userLocation);
            INTERNAL_USER_LOCATION_MAP.put(username, userLocation);
        });
        LOGGER.debug("Created {} internal test userLocations.", getInternalUserNumber());
    }

    private static void generateUserLocationHistory(UserLocation userLocation) {
        userLocation.addToVisitedLocations(new VisitedLocation(userLocation.getUserId(), new GpsUtil().getAttractions().get(0), getRandomTime()));
        IntStream.range(0, 3).forEach(i-> {
            userLocation.addToVisitedLocations(new VisitedLocation(userLocation.getUserId(), new gpsUtil.location.Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
        });
    }

    private static double generateRandomLongitude() {
        double leftLimit = -180;
        double rightLimit = 180;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private static double generateRandomLatitude() {
        double leftLimit = -85.05112878;
        double rightLimit = 85.05112878;
        return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
    }

    private static Date getRandomTime() {
        LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }
}
