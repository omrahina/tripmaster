package com.tripmaster.tourguide.service;

import com.tripmaster.tourguide.data.InternalDataHelper;
import com.tripmaster.tourguide.exceptions.UserNotFoundException;
import com.tripmaster.tourguide.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternalUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalUserService.class);


    public void addUser(User user) {
        if(!InternalDataHelper.getInternalUserMap().containsKey(user.getUsername())) {
            InternalDataHelper.addInternalUser(user);
            LOGGER.info("User {} added", user.getUserId());
        } else{
            LOGGER.warn("User {} already exists", user.getUserId());
        }
    }

    public User findUserByUserName(String userName) throws UserNotFoundException{
        User user = InternalDataHelper.getInternalUserMap().get(userName);
        if (user != null) {
            LOGGER.info("User found");
            return user;
        }
        LOGGER.error("User not found");
        throw new UserNotFoundException("User not found");
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(InternalDataHelper.getInternalUserMap().values());
    }

}
