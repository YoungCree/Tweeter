package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;

public class NumFollowersFollowingServiceProxyTest {

    private NumFollowersFollowingRequest validRequest;
    private NumFollowersFollowingRequest invalidRequest;
    private NumFollowersFollowingResponse successResponse;
    private NumFollowersFollowingResponse failureResponse;

    private NumFollowersFollowingServiceProxy numFolFolSpy;
    private String URL = "/getnumfolfol";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("First", "Last", null);

        validRequest = new NumFollowersFollowingRequest(user);
        invalidRequest = new NumFollowersFollowingRequest(null);

        successResponse = new NumFollowersFollowingResponse(4, 5);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getNumFollowersFollowing(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new NumFollowersFollowingResponse("An Exception Occurred");
        Mockito.when(mockServerFacade.getNumFollowersFollowing(invalidRequest, URL)).thenReturn(failureResponse);

        numFolFolSpy = Mockito.spy(new NumFollowersFollowingServiceProxy());
        Mockito.when(numFolFolSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetNumFolFol_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        NumFollowersFollowingResponse response = numFolFolSpy.getNumFollowersFollowing(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetNumFolFol_validRequest_valuesCorrect() throws IOException, TweeterRemoteException {
        NumFollowersFollowingResponse response = numFolFolSpy.getNumFollowersFollowing(validRequest);
        Assertions.assertEquals(response.getNumFollowers(), 4);
        Assertions.assertEquals(response.getNumFollowing(), 5);
    }

    @Test
    public void testGetNumFolFol_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        NumFollowersFollowingResponse response = numFolFolSpy.getNumFollowersFollowing(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
