package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.model.service.NewTweetServiceProxy;
import model.domain.Status;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;

public class NewTweetTest {

    private NewTweetRequest validRequest;
    private NewTweetRequest invalidRequest;
    private NewTweetServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("FirstName", "LastName", null);
        Status status = new Status("Hello", LocalDateTime.now(), null, null, user);

        validRequest = new NewTweetRequest(status);
        invalidRequest = new NewTweetRequest(null);
        serviceProxy = new NewTweetServiceProxy();
    }

    @Test
    public void testNewTweet_validRequest() throws IOException, TweeterRemoteException {
        NewTweetResponse response = serviceProxy.postTweet(validRequest);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testNewTweet_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.postTweet(invalidRequest);
        });
    }
}
