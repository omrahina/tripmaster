package com.tripmaster.rewards.dto;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationDto {

    private UUID userId;
    private LocationDto location;
    private Date timeVisited;

    public VisitedLocationDto(UUID userId, LocationDto location, Date timeVisited) {
        this.userId = userId;
        this.location = location;
        this.timeVisited = timeVisited;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }
}
