package com.tripmaster.location.service;

import com.tripmaster.location.data.MockLocationDataUtils;
import com.tripmaster.location.exceptions.UserLocationException;
import com.tripmaster.location.model.UserLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserLocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLocationService.class);

    public UserLocation addLocation(UserLocation userLocation) throws UserLocationException{
        if(!MockLocationDataUtils.getInternalUserLocationMap().containsKey(userLocation.getUsername())) {
            MockLocationDataUtils.addLocationEntry(userLocation);
            LOGGER.info("Location added for user {}", userLocation.getUsername());
            return userLocation;
        }
        LOGGER.warn("Location entry for user {} already exists", userLocation.getUsername());

        throw new UserLocationException("Location entry already exists");
    }

    public UserLocation findLocationEntryByUsername(String username) throws UserLocationException {
        UserLocation userLocation = MockLocationDataUtils.getInternalUserLocationMap().get(username);
        if (userLocation != null) {
            LOGGER.info("Location entry found");
            return userLocation;
        }
        LOGGER.warn("Location entry not found");
        throw new UserLocationException("Location entry not found");
    }

    public List<UserLocation> getAllUserLocations() {
        return new ArrayList<>(MockLocationDataUtils.getInternalUserLocationMap().values());
    }

}
