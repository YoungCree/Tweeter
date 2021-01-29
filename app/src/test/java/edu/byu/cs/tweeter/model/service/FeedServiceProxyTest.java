package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import model.domain.Feed;
import model.domain.Status;
import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.FeedRequest;
import model.service.response.FeedResponse;

public class FeedServiceProxyTest {

    private FeedRequest validRequest;
    private FeedRequest invalidRequest;
    private FeedResponse successResponse;
    private FeedResponse failureResponse;

    private FeedServiceProxy feedServiceProxySpy;
    private String URL = "/getfeed";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status resultStatus1 = new Status("test", LocalDateTime.now(), null, null, currentUser);
        Status resultStatus2 = new Status("test2", LocalDateTime.now().minusDays(1), null, null, currentUser);
        Status resultStatus3 = new Status("test3", LocalDateTime.now().minusHours(3), null, null, currentUser);

        validRequest = new FeedRequest(currentUser, 3, null);
        invalidRequest = new FeedRequest(null, 0, null);

        successResponse = new FeedResponse(new Feed(Arrays.asList(resultStatus1, resultStatus2, resultStatus3)), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFeed(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new FeedResponse("An exception occured");
        Mockito.when(mockServerFacade.getFeed(invalidRequest, URL)).thenReturn(failureResponse);

        feedServiceProxySpy = Mockito.spy(new FeedServiceProxy());
        Mockito.when(feedServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceProxySpy.getFeed(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFeed_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceProxySpy.getFeed(validRequest);

        for (int i = 0; i < response.getFeed().getSize(); i++) {
            User tmpUser = response.getFeed().getStatusAt(i).getUser();
            Assertions.assertNotNull(tmpUser.getImageBytes());
        }
    }

    @Test
    public void testGetFeed_invalidRequest_returnsNoFeed() throws IOException, TweeterRemoteException {
        FeedResponse response = feedServiceProxySpy.getFeed(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
