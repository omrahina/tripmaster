package com.tripmaster.tourguide.proxies;

import com.tripmaster.tourguide.beans.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "rewards-microservice", url = "localhost:8091/reward")
public interface UserRewardMicroserviceProxy {

    @RequestMapping("/rewards")
    List<RewardBean> getRewards(@RequestParam String username, @RequestBody List<VisitedLocationBean> visitedLocations);

    @PutMapping("/preferences")
    UserRewardBean updatePreferences(@RequestParam String username, @RequestBody UserPreferencesBean userPreferences);

    @RequestMapping("/nearbyAttractions")
    NearbyAttractionBean getNearbyAttractions(@RequestBody VisitedLocationBean visitedLocation);

    @GetMapping("/tripDeals")
    List<ProviderBean> getTripDeals(@RequestParam String username);

}
