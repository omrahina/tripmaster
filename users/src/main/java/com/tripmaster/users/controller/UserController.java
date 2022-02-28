package com.tripmaster.users.controller;

import com.tripmaster.users.dto.UserDto;
import com.tripmaster.users.exceptions.UserException;
import com.tripmaster.users.model.User;
import com.tripmaster.users.service.InternalUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final InternalUserService internalUserService;

    public UserController(InternalUserService internalUserService) {
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
