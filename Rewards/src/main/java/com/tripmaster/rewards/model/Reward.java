package com.tripmaster.rewards.model;

import com.tripmaster.rewards.dto.VisitedLocationDto;
import gpsUtil.location.Attraction;

public class Reward {

    public final VisitedLocationDto visitedLocation;
    public final Attraction attraction;
    private int rewardPoints;

    public Reward(VisitedLocationDto visitedLocation, Attraction attraction) {
        this.visitedLocation = visitedLocation;
        this.attraction = attraction;
    }

    public Reward(VisitedLocationDto visitedLocation, Attraction attraction, int rewardPoints) {
        this(visitedLocation, attraction);
        this.rewardPoints = rewardPoints;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
