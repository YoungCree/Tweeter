package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowUserServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;

public class FollowUserTest {

    private FollowUserRequest validRequest;
    private FollowUserRequest invalidRequest;
    private FollowUserServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new FollowUserRequest(rootUser, userGiven);
        invalidRequest = new FollowUserRequest(null, null);
        serviceProxy = new FollowUserServiceProxy();
    }

    @Test
    void testGetFollowUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowUserResponse response = serviceProxy.followUser(validRequest);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void testGetFollowUser_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.followUser(invalidRequest);
        });
    }
}
