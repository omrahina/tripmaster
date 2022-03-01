package com.tripmaster.tourguide.controller;

import com.tripmaster.tourguide.beans.UserBean;
import com.tripmaster.tourguide.proxies.UsersMicroserviceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tourguide")
public class TourGuideController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourGuideController.class);

    private final UsersMicroserviceProxy usersProxy;

    public TourGuideController(UsersMicroserviceProxy usersProxy) {
        this.usersProxy = usersProxy;
    }

    @RequestMapping("/addUser")
    public UserBean addUser(@RequestBody UserBean user) {
        UserBean savedUser = usersProxy.addUser(user);
        return savedUser;

    }


}
