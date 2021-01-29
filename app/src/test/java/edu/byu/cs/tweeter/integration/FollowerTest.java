package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowerServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;

public class FollowerTest {

    private FollowerRequest validRequest;
    private FollowerRequest invalidRequest;
    private FollowerServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        validRequest = new FollowerRequest(currentUser, 3, null);
        invalidRequest = new FollowerRequest(null, 0, null);
        serviceProxy = new FollowerServiceProxy();
    }

    @Test
    public void testGetFollowers_validRequest() throws IOException, TweeterRemoteException {
        FollowerResponse response = serviceProxy.getFollowers(validRequest);
        Assertions.assertNotNull(response.getFollowers());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetFollowers_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
           serviceProxy.getFollowers(invalidRequest);
        });
    }
}
