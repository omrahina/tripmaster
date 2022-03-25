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

    /**
     * Add a new location entry
     * @param userLocation the entry to be added
     * @return the saved entry or throws an exception if the entry already exists
     * @throws UserLocationException
     */
    public UserLocation addLocation(UserLocation userLocation) throws UserLocationException{
        if(!MockLocationDataUtils.getInternalUserLocationMap().containsKey(userLocation.getUsername())) {
            MockLocationDataUtils.addLocationEntry(userLocation);
            LOGGER.info("Location added for user {}", userLocation.getUsername());
            return userLocation;
        }
        LOGGER.warn("Location entry for user {} already exists", userLocation.getUsername());

        throw new UserLocationException("Location entry already exists");
    }

    /**
     * Finds the corresponding location entry for a specified user
     * @param username  a string
     * @return the location entry if found
     * @throws UserLocationException
     */
    public UserLocation findLocationEntryByUsername(String username) throws UserLocationException {
        UserLocation userLocation = MockLocationDataUtils.getInternalUserLocationMap().get(username);
        if (userLocation != null) {
            LOGGER.info("Location entry found");
            return userLocation;
        }
        LOGGER.warn("Location entry not found");
        throw new UserLocationException("Location entry not found");
    }

    /**
     * Get all the user location entries
     * @return a list of entries
     */
    public List<UserLocation> getAllUserLocations() {
        return new ArrayList<>(MockLocationDataUtils.getInternalUserLocationMap().values());
    }

}
