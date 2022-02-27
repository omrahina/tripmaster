package com.tripmaster.tourguide.model;

import java.util.Date;
import java.util.UUID;

public class User {

    private final UUID userId;

    private final String username;

    private String phoneNumber;

    private String emailAddress;

    private Date latestLocationTimestamp;

    public User(UUID userId, String username, String phoneNumber, String emailAddress) {
        this.userId = userId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
        this.latestLocationTimestamp = latestLocationTimestamp;
    }

    public Date getLatestLocationTimestamp() {
        return latestLocationTimestamp;
    }
}
