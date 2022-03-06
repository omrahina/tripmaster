package com.tripmaster.rewards.dto;

import java.util.List;

public class NearbyAttractionDto {

    LocationDto userLocation;
    List<AttractionInfo> nearbyAttractions;

    public NearbyAttractionDto(LocationDto userLocation) {
        this.userLocation = userLocation;
    }

    public NearbyAttractionDto(LocationDto userLocation, List<AttractionInfo> nearbyAttractions) {
        this.userLocation = userLocation;
        this.nearbyAttractions = nearbyAttractions;
    }

    public LocationDto getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LocationDto userLocation) {
        this.userLocation = userLocation;
    }

    public List<AttractionInfo> getNearbyAttractions() {
        return nearbyAttractions;
    }

    public void setNearbyAttractions(List<AttractionInfo> nearbyAttractions) {
        this.nearbyAttractions = nearbyAttractions;
    }
}
