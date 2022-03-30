package com.tripmaster.tourguide.controller;

import com.tripmaster.tourguide.beans.*;
import com.tripmaster.tourguide.proxies.UserLocationMicroserviceProxy;
import com.tripmaster.tourguide.proxies.UserRewardMicroserviceProxy;
import com.tripmaster.tourguide.proxies.UsersMicroserviceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tourguide")
public class TourGuideController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourGuideController.class);

    private final UsersMicroserviceProxy usersProxy;

    private final UserLocationMicroserviceProxy userLocationProxy;

    private final UserRewardMicroserviceProxy userRewardProxy;

    public TourGuideController(UsersMicroserviceProxy usersProxy, UserLocationMicroserviceProxy userLocationProxy, UserRewardMicroserviceProxy userRewardProxy) {
        this.usersProxy = usersProxy;
        this.userLocationProxy = userLocationProxy;
        this.userRewardProxy = userRewardProxy;
    }

    @RequestMapping("/addUser")
    public UserBean addUser(@RequestBody UserBean user) {
        UserBean savedUser = usersProxy.addUser(user);
        UserLocationBean location = userLocationProxy.addLocationEntry(user.getUsername());
        UserRewardBean reward = userRewardProxy.addUserRewardEntry(user.getUsername());
        LOGGER.info("location entry " + location.getUserId());
        LOGGER.info("reward entry " + reward.getId());
        return savedUser;
    }

    @RequestMapping("/location")
    public VisitedLocationBean getLocation(@RequestParam String username) {
        return userLocationProxy.getLocation(username);
    }

    @GetMapping("/locations")
    List<VisitedLocationBean> getUserLocations(@RequestParam String username) {
        return userLocationProxy.getUserLocations(username);
    }

    @GetMapping("/allKnownLocations")
    List<LocationHistoryBean> getAllKnownLocations() {
        return userLocationProxy.getAllKnownLocations();
    }

    @RequestMapping("/rewards")
    List getRewards(@RequestParam String username) {
        List<VisitedLocationBean> visitedLocations = userLocationProxy.getUserLocations(username);
        if (CollectionUtils.isEmpty(visitedLocations)) {
            return visitedLocations;
        }
        return userRewardProxy.getRewards(username, visitedLocations);
    }

    @PutMapping("/preferences")
    UserPreferencesBean updatePreferences(@RequestParam String username, @RequestBody UserPreferencesBean userPreferences) {
        return userRewardProxy.updatePreferences(username, userPreferences);
    }

    @RequestMapping("/nearbyAttractions")
    NearbyAttractionBean getNearbyAttractions(@RequestParam String username) {
        return userRewardProxy.getNearbyAttractions(userLocationProxy.getLocation(username));
    }

    @GetMapping("/tripDeals")
    List<ProviderBean> getTripDeals(@RequestParam String username) {
        return userRewardProxy.getTripDeals(username);
    }

}
