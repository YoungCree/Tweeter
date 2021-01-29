package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import model.domain.Status;
import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;

public class NewTweetServiceProxyTest {

    private NewTweetRequest validRequest;
    private NewTweetRequest invalidRequest;
    private NewTweetResponse successResponse;
    private NewTweetResponse failureResponse;

    private NewTweetServiceProxy newTweetServiceProxySpy;
    private String URL = "/posttweet";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("First", "Last", null);
        Status status = new Status("Hello", LocalDateTime.now(), null, null, user);

        validRequest = new NewTweetRequest(status);
        invalidRequest = new NewTweetRequest(null);

        successResponse = new NewTweetResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.postTweet(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new NewTweetResponse("An Exception occurred");
        Mockito.when(mockServerFacade.postTweet(invalidRequest, URL)).thenReturn(failureResponse);

        newTweetServiceProxySpy = Mockito.spy(new NewTweetServiceProxy());
        Mockito.when(newTweetServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testNewTweet_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        NewTweetResponse response = newTweetServiceProxySpy.postTweet(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testNewTweet_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        NewTweetResponse response = newTweetServiceProxySpy.postTweet(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
