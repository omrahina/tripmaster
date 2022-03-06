package com.tripmaster.tourguide.beans;

public class AttractionInfoBean {

    private AttractionBean attraction;
    private double distanceFromUserLocation;
    private int rewardPoints;

    public AttractionBean getAttraction() {
        return attraction;
    }

    public void setAttraction(AttractionBean attraction) {
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
