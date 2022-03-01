package com.tripmaster.tourguide.beans;

import java.util.Date;
import java.util.UUID;

public class VisitedLocationBean {

    private UUID userId;
    private LocationBean location;
    private Date timeVisited;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public Date getTimeVisited() {
        return timeVisited;
    }

    public void setTimeVisited(Date timeVisited) {
        this.timeVisited = timeVisited;
    }
}
