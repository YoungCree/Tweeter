package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;

public class UnfollowUserServiceProxyTest {

    private UnfollowUserRequest validRequest;
    private UnfollowUserRequest invalidRequest;
    private UnfollowUserResponse successResponse;
    private UnfollowUserResponse failureResponse;

    private UnfollowUserServiceProxy unfollowUserServiceProxySpy;
    private String URL = "/unfollowuser";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new UnfollowUserRequest(rootUser, userGiven);
        invalidRequest = new UnfollowUserRequest(null, null);

        successResponse = new UnfollowUserResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.unfollowUser(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new UnfollowUserResponse("An Exception occured");
        Mockito.when(mockServerFacade.unfollowUser(invalidRequest, URL)).thenReturn(failureResponse);

        unfollowUserServiceProxySpy = Mockito.spy(new UnfollowUserServiceProxy());
        Mockito.when(unfollowUserServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetUnfollowUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UnfollowUserResponse response = unfollowUserServiceProxySpy.unfollowUser(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetUnfollowUser_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        UnfollowUserResponse response = unfollowUserServiceProxySpy.unfollowUser(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
