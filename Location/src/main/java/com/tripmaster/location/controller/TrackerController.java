package com.tripmaster.location.controller;

import com.tripmaster.location.dto.LocationHistoryDto;
import com.tripmaster.location.exceptions.UserLocationException;
import com.tripmaster.location.model.UserLocation;
import com.tripmaster.location.service.TrackerService;
import com.tripmaster.location.service.UserLocationService;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/tracker")
public class TrackerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrackerController.class);

    private final TrackerService trackerService;

    private final UserLocationService userLocationService;

    public TrackerController(TrackerService trackerService, UserLocationService userLocationService) {
        this.trackerService = trackerService;
        this.userLocationService = userLocationService;
        Locale.setDefault(new Locale("en", "US"));
    }

    @GetMapping("/location")
    public ResponseEntity<VisitedLocation> getLocation(@RequestParam String username) {
        VisitedLocation visitedLocation = null;
        UserLocation location = null;
        try {
            location = getUserLocation(username);
            if (location == null) {
                location = userLocationService.addLocation(new UserLocation(UUID.randomUUID(), username));
            }
            visitedLocation = trackerService.getUserLocation(location);
        } catch (UserLocationException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Please retry", e);
        }
        LOGGER.info("User location detected");
        return new ResponseEntity<>(visitedLocation, HttpStatus.OK);
    }

    @GetMapping("/allKnownLocations")
    public ResponseEntity<List<LocationHistoryDto>> getAllKnownLocations() {
        List<UserLocation> locations = userLocationService.getAllUserLocations();
        List<LocationHistoryDto> locationHistory = trackerService.getAllKnownLocations(locations);
        if (locationHistory != null) {
            return new ResponseEntity<>(locationHistory, HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location history empty");
    }

    @GetMapping("/locations")
    public ResponseEntity<List<VisitedLocation>> getUserLocations(@RequestParam String username) {
        UserLocation location = getUserLocation(username);
        if (location != null) {
            return new ResponseEntity<>(location.getVisitedLocations(), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No location entry found for " + username);
    }

    private UserLocation getUserLocation(String username) {
        return userLocationService.findLocationEntryByUsername(username);
    }

}
