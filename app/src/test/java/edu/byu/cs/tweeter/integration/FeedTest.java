package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.service.FeedServiceProxy;
import model.domain.Feed;
import model.domain.Status;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.FeedRequest;
import model.service.response.FeedResponse;

public class FeedTest {

    private FeedRequest validRequest;
    private FeedRequest invalidRequest;
    private FeedServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        validRequest = new FeedRequest(currentUser, 3, null);
        invalidRequest = new FeedRequest(null, 0, null);
        serviceProxy = new FeedServiceProxy();
    }

    @Test
    public void testGetFeed_validRequest() throws IOException, TweeterRemoteException {
        FeedResponse response = serviceProxy.getFeed(validRequest);
        Assertions.assertNotNull(response.getFeed());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test void testGetFeed_invalidRequest() {
            Assertions.assertThrows(TweeterRemoteException.class, () -> {
                serviceProxy.getFeed(invalidRequest);
            });

    }
}
