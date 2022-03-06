package com.tripmaster.rewards.dto;

import gpsUtil.location.Attraction;

public class AttractionInfo {

    private Attraction attraction;
    private double distanceFromUserLocation;
    private int rewardPoints;

    public AttractionInfo(Attraction attraction, double distanceFromUserLocation, int rewardPoints) {
        this.attraction = attraction;
        this.distanceFromUserLocation = distanceFromUserLocation;
        this.rewardPoints = rewardPoints;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public double getDistanceFromUserLocation() {
        return distanceFromUserLocation;
    }

    public void setDistanceFromUserLocation(double distanceFromUserLocation) {
        this.distanceFromUserLocation = distanceFromUserLocation;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
