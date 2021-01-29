package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowStateServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;

public class FollowStateTest {

    private FollowStateRequest validRequest;
    private FollowStateRequest invalidRequest;
    private FollowStateServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new FollowStateRequest(rootUser, userGiven);
        invalidRequest = new FollowStateRequest(null, null);
        serviceProxy = new FollowStateServiceProxy();
    }

    @Test
    public void testGetFollowState_validRequest() throws IOException, TweeterRemoteException {
        FollowStateResponse response = serviceProxy.followState(validRequest);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetFollowState_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.followState(invalidRequest);
        });
    }
}
