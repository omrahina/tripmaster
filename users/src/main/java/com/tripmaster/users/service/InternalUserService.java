package com.tripmaster.users.service;

import com.tripmaster.users.data.InternalDataHelper;
import com.tripmaster.users.exceptions.UserException;
import com.tripmaster.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternalUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalUserService.class);

    /**
     * Add a new user
     * @param user information concerning the user to be added
     * @return the saved user
     * @throws UserException
     */
    public User addUser(User user) throws UserException {
        if(!InternalDataHelper.getInternalUserMap().containsKey(user.getUsername())) {
            InternalDataHelper.addInternalUser(user);
            LOGGER.info("User {} added", user.getUserId());
            return user;
        }
        LOGGER.warn("User {} already exists", user.getUserId());
        throw new UserException("User already exists");
    }

    /**
     * Finds a user by name
     * @param username a string
     * @return the corresponding user
     * @throws UserException
     */
    public User findUserByUserName(String username) throws UserException {
        User user = InternalDataHelper.getInternalUserMap().get(username);
        if (user != null) {
            LOGGER.info("User found");
            return user;
        }
        LOGGER.error("User not found");
        throw new UserException("User not found");
    }

    /**
     * Returns all the users
     * @return a list of users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(InternalDataHelper.getInternalUserMap().values());
    }

}
