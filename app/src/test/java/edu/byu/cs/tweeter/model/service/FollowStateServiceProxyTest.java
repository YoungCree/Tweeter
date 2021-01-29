package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;

public class FollowStateServiceProxyTest {

    private FollowStateRequest validRequest;
    private FollowStateRequest invalidRequest;
    private FollowStateResponse successResponse;
    private FollowStateResponse failureResponse;

    private FollowStateServiceProxy followStateServiceProxySpy;
    private String URL = "/getfollowstate";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new FollowStateRequest(rootUser, userGiven);
        invalidRequest = new FollowStateRequest(null, null);

        successResponse = new FollowStateResponse(false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.followState(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new FollowStateResponse("An Exception occured");
        Mockito.when(mockServerFacade.followState(invalidRequest, URL)).thenReturn(failureResponse);

        followStateServiceProxySpy = Mockito.spy(new FollowStateServiceProxy());
        Mockito.when(followStateServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowState_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowStateResponse response = followStateServiceProxySpy.followState(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowState_validRequest_getsBool() throws IOException, TweeterRemoteException {
        FollowStateResponse response = followStateServiceProxySpy.followState(validRequest);
        Assertions.assertFalse(response.getIsFollowing());
    }

    @Test
    public void testGetFollowState_invalidRequest_returnsNoState() throws IOException, TweeterRemoteException {
        FollowStateResponse response = followStateServiceProxySpy.followState(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
