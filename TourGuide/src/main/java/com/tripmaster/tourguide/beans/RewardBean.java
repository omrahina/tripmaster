package com.tripmaster.tourguide.beans;

public class RewardBean {

    private VisitedLocationBean visitedLocation;
    private AttractionBean attraction;
    private int rewardPoints;

    public VisitedLocationBean getVisitedLocation() {
        return visitedLocation;
    }

    public void setVisitedLocation(VisitedLocationBean visitedLocation) {
        this.visitedLocation = visitedLocation;
    }

    public AttractionBean getAttraction() {
        return attraction;
    }

    public void setAttraction(AttractionBean attraction) {
        this.attraction = attraction;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
