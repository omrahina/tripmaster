package com.tripmaster.rewards.service;

import com.tripmaster.rewards.data.MockRewardDataUtils;
import com.tripmaster.rewards.model.UserReward;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRewardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRewardService.class);

    /**
     * Add a new reward entry
     * @param userReward the new entry
     * @return the saved entry or null if the entry already exists
     */
    public UserReward addUserReward(UserReward userReward) {
        if (!MockRewardDataUtils.getInternalUserRewardMap().containsKey(userReward.getUsername())) {
            MockRewardDataUtils.addRewardEntry(userReward);
            LOGGER.info("Reward entry added for user {}", userReward.getUsername());
            return userReward;
        }
        LOGGER.warn("Reward entry for user {} already exists", userReward.getUsername());

        return null;
    }

    /**
     * Updates a user reward entry
     * @param userReward the concerned reward entry
     * @return updated entry
     */
    public UserReward updateUserReward(UserReward userReward) {
        MockRewardDataUtils.addRewardEntry(userReward);
        LOGGER.info("Reward Entry updated");
        return userReward;
    }

    /**
     * Finds the corresponding reward entry for a specified user
     * @param username the username
     * @return the reward entry or null
     */
    public UserReward findRewardEntryByUsername(String username) {
        UserReward userReward = MockRewardDataUtils.getInternalUserRewardMap().get(username);
        if (userReward != null) {
            LOGGER.info("Reward entry found");
            return userReward;
        }
        LOGGER.warn("Reward entry not found");
        return null;
    }

    /**
     * Returns all reward entries
     * @return a list of entries
     */
    public List<UserReward> getAllUserRewardEntries() {
        return  new ArrayList<>(MockRewardDataUtils.getInternalUserRewardMap().values());
    }

}
