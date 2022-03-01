package com.tripmaster.tourguide.beans;

import java.util.List;
import java.util.UUID;

public class LocationHistoryBean {

    private UUID userId;
    private List<LocationBean> visitedLocations;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<LocationBean> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<LocationBean> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }
}
