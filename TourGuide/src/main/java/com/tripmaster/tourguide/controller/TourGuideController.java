package com.tripmaster.tourguide.controller;

import com.tripmaster.tourguide.dto.UserDto;
import com.tripmaster.tourguide.exceptions.UserException;
import com.tripmaster.tourguide.model.User;
import com.tripmaster.tourguide.service.InternalUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown user", e);
        }

    }

    @PostMapping("/user")
    public ResponseEntity<User> addUser(@RequestBody UserDto user) {
        try {
            User savedUser = internalUserService.addUser(new User(user.getUsername(), user.getPhoneNumber(), user.getEmailAddress()));
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (UserException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }

    }

}
