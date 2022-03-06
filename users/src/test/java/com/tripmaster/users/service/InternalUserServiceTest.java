package com.tripmaster.users.service;

import com.tripmaster.users.data.InternalDataHelper;
import com.tripmaster.users.exceptions.UserException;
import com.tripmaster.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InternalUserServiceTest {

    @InjectMocks
    private InternalUserService internalUserService;


    @BeforeEach
    public void setUpEachTest() {
        InternalDataHelper.initializeInternalUsers();
    }

    @Test
    public void addUser_create_ok() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        internalUserService.addUser(user);
        internalUserService.addUser(user2);

        assertEquals(user, InternalDataHelper.getInternalUserMap().get(user.getUsername()));
        assertEquals(user2, InternalDataHelper.getInternalUserMap().get(user2.getUsername()));
        assertEquals(InternalDataHelper.getInternalUserNumber() + 2, InternalDataHelper.getInternalUserMap().size());
    }

    @Test
    public void addUser_already_exists_exception() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

        assertNull(InternalDataHelper.getInternalUserMap().get(user.getUsername()));
        assertEquals(InternalDataHelper.getInternalUserNumber() , InternalDataHelper.getInternalUserMap().size());

        internalUserService.addUser(user);

        assertEquals(user, InternalDataHelper.getInternalUserMap().get(user.getUsername()));
        assertEquals(InternalDataHelper.getInternalUserNumber() + 1, InternalDataHelper.getInternalUserMap().size());

        assertThatExceptionOfType(UserException.class).isThrownBy(()
                -> internalUserService.addUser(user))
                .withMessage("User already exists");
    }

    @Test
    public void findUserByUserName_should_return_a_user() {
        User user = internalUserService.findUserByUserName("internalUser0");

        assertNotNull(user);
    }

    @Test
    public void findUserByUserName_should_throw_UserNotFoundException() {
        assertThatExceptionOfType(UserException.class).isThrownBy(()
                -> internalUserService.findUserByUserName("random"))
                .withMessage("User not found");
    }

    @Test
    public void getAllUsers_ok() {
        User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
        User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

        internalUserService.addUser(user);
        internalUserService.addUser(user2);

        List<User> allUsers = internalUserService.getAllUsers();

        assertTrue(allUsers.contains(user));
        assertTrue(allUsers.contains(user2));
    }

}
