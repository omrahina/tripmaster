package com.tripmaster.rewards.data;

import com.tripmaster.rewards.model.UserReward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class MockRewardDataUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockRewardDataUtils.class);
    private static final Map<String, UserReward> INTERNAL_USER_REWARD_MAP = new HashMap<>();
    private static int internalUserNumber;

    public static Map<String, UserReward> getInternalUserRewardMap() {
        return INTERNAL_USER_REWARD_MAP;
    }

    public static void addRewardEntry(UserReward userReward) {
        INTERNAL_USER_REWARD_MAP.put(userReward.getUsername(), userReward);
    }

    public static int getInternalUserNumber() {
        return internalUserNumber;
    }

    public static void setInternalUserNumber(int internalUserNumber) {
        MockRewardDataUtils.internalUserNumber = internalUserNumber;
    }

    public static void initializeInternalRewards() {
        LOGGER.info("Initializing locations");
        INTERNAL_USER_REWARD_MAP.clear();
        IntStream.range(0, MockRewardDataUtils.getInternalUserNumber()).forEach(i -> {
            String username = "internalUser" + i;
            UserReward userReward = new UserReward(UUID.randomUUID(), username);
            INTERNAL_USER_REWARD_MAP.put(username, userReward);
        });
    }

}
