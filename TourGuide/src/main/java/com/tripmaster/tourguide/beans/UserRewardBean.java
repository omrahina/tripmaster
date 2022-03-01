package com.tripmaster.tourguide.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserRewardBean {

    private UUID id;
    private String username;
    private List<RewardBean> rewards = new ArrayList<>();
    private UserPreferencesBean userPreferences = new UserPreferencesBean();
    private List<ProviderBean> tripDeals = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<RewardBean> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardBean> rewards) {
        this.rewards = rewards;
    }

    public UserPreferencesBean getUserPreferences() {
        return userPreferences;
    }

    public void setUserPreferences(UserPreferencesBean userPreferences) {
        this.userPreferences = userPreferences;
    }

    public List<ProviderBean> getTripDeals() {
        return tripDeals;
    }

    public void setTripDeals(List<ProviderBean> tripDeals) {
        this.tripDeals = tripDeals;
    }
}
