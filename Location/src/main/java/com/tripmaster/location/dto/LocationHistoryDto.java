package com.tripmaster.location.dto;

import gpsUtil.location.Location;

import java.util.List;
import java.util.UUID;

public class LocationHistoryDto {

    private UUID userId;
    private List<Location> visitedLocations;

    public LocationHistoryDto(UUID userId, List<Location> visitedLocations) {
        this.userId = userId;
        this.visitedLocations = visitedLocations;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<Location> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<Location> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }
}
