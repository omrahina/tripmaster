package com.tripmaster.rewards.model;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class UserPreferences {

    private int attractionProximity;
    private final CurrencyUnit currency;
    private final Money lowerPricePoint;
    private final Money highPricePoint;
    private int tripDuration = 1;
    private int ticketQuantity = 1;
    private int numberOfAdults = 1;
    private int numberOfChildren = 0;

    public UserPreferences() {
        attractionProximity = Integer.MAX_VALUE;
        currency = Monetary.getCurrency("USD");
        lowerPricePoint = Money.of(0, currency);
        highPricePoint = Money.of(Integer.MAX_VALUE, currency);
    }

    public UserPreferences(int tripDuration, int ticketQuantity, int numberOfAdults, int numberOfChildren) {
        this();
        this.tripDuration = tripDuration;
        this.ticketQuantity = ticketQuantity;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
    }

    public int getAttractionProximity() {
        return attractionProximity;
    }

    public void setAttractionProximity(int attractionProximity) {
        this.attractionProximity = attractionProximity;
    }

    public CurrencyUnit getCurrency() {
        return currency;
    }

    public Money getLowerPricePoint() {
        return lowerPricePoint;
    }

    public Money getHighPricePoint() {
        return highPricePoint;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }

    public int getNumberOfAdults() {
        return numberOfAdults;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }
}
