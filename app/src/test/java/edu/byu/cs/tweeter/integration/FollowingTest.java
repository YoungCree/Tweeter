package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.model.service.FollowingServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.FollowingRequest;
import model.service.response.FollowingResponse;

public class FollowingTest {

    private FollowingRequest validRequest;
    private FollowingRequest invalidRequest;
    private FollowingServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        // Setup request objects to use in the tests
        validRequest = new FollowingRequest(currentUser, 3, null);
        invalidRequest = new FollowingRequest(null, 0, null);
        serviceProxy = new FollowingServiceProxy();
    }

    @Test
    public void testGetFollowees_validRequest() throws IOException, TweeterRemoteException {
        FollowingResponse response = serviceProxy.getFollowees(validRequest);
        Assertions.assertNotNull(response.getFollowees());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetFollowees_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
           serviceProxy.getFollowees(invalidRequest);
        });
    }
}
