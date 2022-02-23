package com.tripmaster.location.model;

import gpsUtil.location.VisitedLocation;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserLocation {

    private final UUID userId;

    private final String username;

    private List<VisitedLocation> visitedLocations = new CopyOnWriteArrayList<>();

    public UserLocation(UUID userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public List<VisitedLocation> getVisitedLocations() {
        return visitedLocations;
    }

    public VisitedLocation getLastVisitedLocation() {
        return visitedLocations.get(visitedLocations.size() - 1);
    }

    public void addToVisitedLocations(VisitedLocation visitedLocation) {
        visitedLocations.add(visitedLocation);
    }
}
