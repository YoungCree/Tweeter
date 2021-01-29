package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.NumFollowersFollowingServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;

public class NumFollowersFollowingTest {

    private NumFollowersFollowingRequest validRequest;
    private NumFollowersFollowingRequest invalidRequest;
    private NumFollowersFollowingServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("First", "Last", null);

        validRequest = new NumFollowersFollowingRequest(user);
        invalidRequest = new NumFollowersFollowingRequest(null);
        serviceProxy = new NumFollowersFollowingServiceProxy();
    }

    @Test
    public void testGetNumFolFol_validRequest() throws IOException, TweeterRemoteException {
        NumFollowersFollowingResponse response = serviceProxy.getNumFollowersFollowing(validRequest);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetNumFolFol_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.getNumFollowersFollowing(invalidRequest);
        });
    }
}
