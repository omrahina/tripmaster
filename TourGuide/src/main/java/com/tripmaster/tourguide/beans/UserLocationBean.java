package com.tripmaster.tourguide.beans;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserLocationBean {

    private UUID userId;

    private String username;

    private List<VisitedLocationBean> visitedLocations = new CopyOnWriteArrayList<>();

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<VisitedLocationBean> getVisitedLocations() {
        return visitedLocations;
    }

    public void setVisitedLocations(List<VisitedLocationBean> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }
}
