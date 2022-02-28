package com.tripmaster.users.data;

import com.tripmaster.users.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class InternalDataHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalDataHelper.class);
    private static final Map<String, User> INTERNAL_USER_MAP = new HashMap<>();
    private static int internalUserNumber = 100;

    static {
        initializeInternalUsers();
    }
    public static Map<String, User> getInternalUserMap() {
        return INTERNAL_USER_MAP;
    }

    public static void addInternalUser(User user) {
        INTERNAL_USER_MAP.put(user.getUsername(), user);
    }

    public static void setInternalUserNumber(int internalUserNumber) {
        InternalDataHelper.internalUserNumber = internalUserNumber;
    }

    public static int getInternalUserNumber() {
        return internalUserNumber;
    }

    public static void initializeInternalUsers() {
        LOGGER.info("Initializing users");
        INTERNAL_USER_MAP.clear();
        IntStream.range(0, InternalDataHelper.getInternalUserNumber()).forEach(i -> {
            String username = "internalUser" + i;
            String phone = "000";
            String email = username + "@tourGuide.com";
            User user = new User(UUID.randomUUID(), username, phone, email);

            INTERNAL_USER_MAP.put(username, user);
        });
        LOGGER.debug("Created {} internal test users.", getInternalUserNumber());
        LOGGER.debug("Finished initializing users");
    }

}
