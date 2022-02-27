package com.tripmaster.rewards.model;

import tripPricer.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserReward {

    private final UUID id;
    private final String username;
    private List<Reward> rewards = new ArrayList<>();
    private UserPreferences userPreferences = new UserPreferences();
    private List<Provider> tripDeals = new ArrayList<>();

    public UserReward(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void addReward(Reward reward) {
        if(rewards.stream().filter(r -> !r.attraction.attractionName.equals(reward.attraction)).count() == 0) {
            rewards.add(reward);
        }
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }

    public List<Provider> getTripDeals() {
        return tripDeals;
    }

    public void setTripDeals(List<Provider> tripDeals) {
        this.tripDeals = tripDeals;
    }
}
