package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.UnfollowUserServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;

public class UnfollowUserTest {

    private UnfollowUserRequest validRequest;
    private UnfollowUserRequest invalidRequest;
    private UnfollowUserServiceProxy serviceProxy;


    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new UnfollowUserRequest(rootUser, userGiven);
        invalidRequest = new UnfollowUserRequest(null, null);
        serviceProxy = new UnfollowUserServiceProxy();
    }

    @Test
    public void testGetUnfollowUser_validRequest() throws IOException, TweeterRemoteException {
        UnfollowUserResponse response = serviceProxy.unfollowUser(validRequest);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetUnfollowUser_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.unfollowUser(invalidRequest);
        });
    }
}
