package com.tripmaster.tourguide.proxies;

import com.tripmaster.tourguide.beans.RewardBean;
import com.tripmaster.tourguide.beans.UserPreferencesBean;
import com.tripmaster.tourguide.beans.UserRewardBean;
import com.tripmaster.tourguide.beans.VisitedLocationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "rewards-microservice", url = "localhost:8091/reward")
public interface UserRewardMicroserviceProxy {

    @GetMapping("/rewards")
    List<RewardBean> getRewards(@RequestParam String username, @RequestBody List<VisitedLocationBean> visitedLocations);

    @PutMapping("/preferences")
    UserRewardBean updatePreferences(@RequestParam String username, @RequestBody UserPreferencesBean userPreferences);

}
