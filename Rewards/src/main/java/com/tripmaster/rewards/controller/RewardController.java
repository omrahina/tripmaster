package com.tripmaster.rewards.controller;

import com.tripmaster.rewards.dto.UserPreferencesDto;
import com.tripmaster.rewards.dto.VisitedLocationDto;
import com.tripmaster.rewards.exceptions.NoRewardException;
import com.tripmaster.rewards.model.Reward;
import com.tripmaster.rewards.model.UserReward;
import com.tripmaster.rewards.service.RewardService;
import com.tripmaster.rewards.service.UserRewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/reward")
public class RewardController {

    private final Logger LOGGER = LoggerFactory.getLogger(RewardController.class);

    private final RewardService rewardService;

    private final UserRewardService userRewardService;

    public RewardController(RewardService rewardService, UserRewardService userRewardService) {
        this.rewardService = rewardService;
        this.userRewardService = userRewardService;
    }

    @GetMapping("/rewards")
    ResponseEntity<List<Reward>> getRewards(@RequestParam String username, @RequestBody List<VisitedLocationDto> visitedLocations) {
        UserReward userRewardEntry = getUserRewardEntry(username);
        if (userRewardEntry == null) {
            userRewardEntry = userRewardService.addUserReward(new UserReward(UUID.randomUUID(), username));
        }
        try {
            List<Reward> rewards = rewardService.getUserRewards(userRewardEntry, visitedLocations);
            return new ResponseEntity<>(rewards, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred! Please retry", e);
        } catch (NoRewardException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }

    @PutMapping("/preferences")
    ResponseEntity<UserReward> updatePreferences(@RequestParam String username, @RequestBody UserPreferencesDto userPreferences) {
        UserReward userRewardEntry = getUserRewardEntry(username);
        if (userRewardEntry != null) {
            UserReward updatedUserReward = rewardService.updatePreferences(userRewardEntry, userPreferences);
            userRewardService.addUserReward(updatedUserReward);
            return new ResponseEntity<>(updatedUserReward, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No reward entry found for " + username);
    }

    private UserReward getUserRewardEntry(String username) {
        return userRewardService.findRewardEntryByUsername(username);
    }

}
