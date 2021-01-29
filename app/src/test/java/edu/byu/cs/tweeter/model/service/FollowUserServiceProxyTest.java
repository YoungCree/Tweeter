package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;

public class FollowUserServiceProxyTest {

    private FollowUserRequest validRequest;
    private FollowUserRequest invalidRequest;
    private FollowUserResponse successResponse;
    private FollowUserResponse failureResponse;

    private FollowUserServiceProxy followUserServiceProxySpy;
    private String URL = "/followuser";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new FollowUserRequest(rootUser, userGiven);
        invalidRequest = new FollowUserRequest(null, null);

        successResponse = new FollowUserResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.followUser(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new FollowUserResponse("An Exception occured");
        Mockito.when(mockServerFacade.followUser(invalidRequest, URL)).thenReturn(failureResponse);

        followUserServiceProxySpy = Mockito.spy(new FollowUserServiceProxy());
        Mockito.when(followUserServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowUserResponse response = followUserServiceProxySpy.followUser(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowUser_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        FollowUserResponse response = followUserServiceProxySpy.followUser(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
