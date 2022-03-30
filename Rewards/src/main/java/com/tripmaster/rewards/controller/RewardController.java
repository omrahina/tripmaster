package com.tripmaster.rewards.controller;

import com.tripmaster.rewards.dto.NearbyAttractionDto;
import com.tripmaster.rewards.dto.UserPreferencesDto;
import com.tripmaster.rewards.dto.VisitedLocationDto;
import com.tripmaster.rewards.exceptions.NoDealOrRewardException;
import com.tripmaster.rewards.model.Reward;
import com.tripmaster.rewards.model.UserPreferences;
import com.tripmaster.rewards.model.UserReward;
import com.tripmaster.rewards.service.RewardService;
import com.tripmaster.rewards.service.UserRewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tripPricer.Provider;

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

    @PostMapping("/userRewardEntry")
    ResponseEntity<UserReward> addUserRewardEntry(@RequestParam String username) {
        UserReward userRewardEntry = userRewardService.addUserReward(new UserReward(UUID.randomUUID(), username));
        if (userRewardEntry != null) {
            return new ResponseEntity<>(userRewardEntry, HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Reward entry already exists ");
    }

    @RequestMapping("/rewards")
    ResponseEntity<List<Reward>> getRewards(@RequestParam String username, @RequestBody List<VisitedLocationDto> visitedLocations) {
        UserReward userRewardEntry = getUserRewardEntry(username);
        if (userRewardEntry != null) {
            try {
                List<Reward> rewards = rewardService.getUserRewards(userRewardEntry, visitedLocations);
                return new ResponseEntity<>(rewards, HttpStatus.OK);
            } catch (InterruptedException | ExecutionException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred! Please retry", e);
            } catch (NoDealOrRewardException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No reward entry found for " + username);

    }

    @PutMapping("/preferences")
    ResponseEntity<UserPreferences> updatePreferences(@RequestParam String username, @RequestBody UserPreferencesDto userPreferences) {
        UserReward userRewardEntry = getUserRewardEntry(username);
        if (userRewardEntry != null) {
            UserPreferences updatedUserPreferences = rewardService.updatePreferences(userRewardEntry, userPreferences);
            return new ResponseEntity<>(updatedUserPreferences, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No reward entry found for " + username);
    }

    @RequestMapping("/nearbyAttractions")
    public ResponseEntity<NearbyAttractionDto> getNearbyAttractions(@RequestBody VisitedLocationDto visitedLocation) {
        NearbyAttractionDto nearbyAttractionDto = rewardService.getNearByAttractions(visitedLocation);
        if (!nearbyAttractionDto.getNearbyAttractions().isEmpty()) {
            return new ResponseEntity<>(nearbyAttractionDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(nearbyAttractionDto, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/tripDeals")
    public ResponseEntity<List<Provider>> getTripDeals(@RequestParam String username) {
        UserReward userRewardEntry = getUserRewardEntry(username);
        if (userRewardEntry != null) {
            try {
                List<Provider> providers = rewardService.getTripDeals(userRewardEntry);
                return new ResponseEntity<>(providers, HttpStatus.OK);
            } catch (NoDealOrRewardException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, e.getMessage(), e);
            }

        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No reward entry found for " + username);
    }

    private UserReward getUserRewardEntry(String username) {
        return userRewardService.findRewardEntryByUsername(username);
    }

}
