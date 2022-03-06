package com.tripmaster.tourguide.beans;

import java.util.List;

public class NearbyAttractionBean {

    private LocationBean userLocation;
    private List<AttractionInfoBean> nearbyAttractions;

    public LocationBean getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LocationBean userLocation) {
        this.userLocation = userLocation;
    }

    public List<AttractionInfoBean> getNearbyAttractions() {
        return nearbyAttractions;
    }

    public void setNearbyAttractions(List<AttractionInfoBean> nearbyAttractions) {
        this.nearbyAttractions = nearbyAttractions;
    }
}
