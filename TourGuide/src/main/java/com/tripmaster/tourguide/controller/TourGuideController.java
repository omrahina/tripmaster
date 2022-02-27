package com.tripmaster.tourguide.controller;

import com.tripmaster.tourguide.exceptions.UserNotFoundException;
import com.tripmaster.tourguide.model.User;
import com.tripmaster.tourguide.service.InternalUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/tourguide")
public class TourGuideController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TourGuideController.class);

    private final InternalUserService internalUserService;

    public TourGuideController(InternalUserService internalUserService) {
        this.internalUserService = internalUserService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam String username) {
        try {
            User user = internalUserService.findUserByUserName(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown user", e);
        }

    }

}
