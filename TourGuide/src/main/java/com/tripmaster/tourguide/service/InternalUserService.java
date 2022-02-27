package com.tripmaster.tourguide.service;

import com.tripmaster.tourguide.data.InternalDataHelper;
import com.tripmaster.tourguide.exceptions.UserException;
import com.tripmaster.tourguide.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternalUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalUserService.class);


    public User addUser(User user) throws UserException{
        if(!InternalDataHelper.getInternalUserMap().containsKey(user.getUsername())) {
            InternalDataHelper.addInternalUser(user);
            LOGGER.info("User {} added", user.getUserId());
            return user;
        }
        LOGGER.warn("User {} already exists", user.getUserId());
        throw new UserException("User already exists");
    }

    public User findUserByUserName(String userName) throws UserException {
        User user = InternalDataHelper.getInternalUserMap().get(userName);
        if (user != null) {
            LOGGER.info("User found");
            return user;
        }
        LOGGER.error("User not found");
        throw new UserException("User not found");
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(InternalDataHelper.getInternalUserMap().values());
    }

}
