package com.tripmaster.tourguide.proxies;

import com.tripmaster.tourguide.beans.LocationHistoryBean;
import com.tripmaster.tourguide.beans.UserLocationBean;
import com.tripmaster.tourguide.beans.VisitedLocationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "location-microservice", url = "location:8090/tracker")
public interface UserLocationMicroserviceProxy {

    @PostMapping("/location")
    UserLocationBean addLocationEntry(@RequestParam String username);

    @GetMapping("/location")
    VisitedLocationBean getLocation(@RequestParam String username);

    @GetMapping("/locations")
    List<VisitedLocationBean> getUserLocations(@RequestParam String username);

    @GetMapping("/allKnownLocations")
    List<LocationHistoryBean> getAllKnownLocations();

}
