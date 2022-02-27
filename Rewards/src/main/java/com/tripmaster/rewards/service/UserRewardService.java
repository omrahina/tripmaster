package com.tripmaster.rewards.service;

import com.tripmaster.rewards.data.MockRewardDataUtils;
import com.tripmaster.rewards.model.UserReward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserRewardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRewardService.class);

    public UserReward addUserReward(UserReward userReward) {
        if (!MockRewardDataUtils.getInternalUserRewardMap().containsKey(userReward.getUsername())) {
            MockRewardDataUtils.addRewardEntry(userReward);
            LOGGER.info("Reward entry added for user {}", userReward.getUsername());
            return userReward;
        }
        LOGGER.warn("Reward entry for user {} already exists", userReward.getUsername());

        return null;
    }

    public UserReward updateUserReward(UserReward userReward) {
        MockRewardDataUtils.addRewardEntry(userReward);
        LOGGER.info("Reward Entry updated");
        return userReward;
    }

    public UserReward findRewardEntryByUsername(String username) {
        UserReward userReward = MockRewardDataUtils.getInternalUserRewardMap().get(username);
        if (userReward != null) {
            LOGGER.info("Reward entry found");
            return userReward;
        }
        LOGGER.warn("Reward entry not found");
        return null;
    }

}
