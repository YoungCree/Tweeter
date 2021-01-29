package edu.byu.cs.tweeter.integration;

import androidx.annotation.StringRes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.ResolverStyle;

import edu.byu.cs.tweeter.model.service.StoryServiceProxy;
import model.domain.Status;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;

public class StoryTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;
    private StoryServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        validRequest = new StoryRequest(currentUser, 3, null);
        invalidRequest = new StoryRequest(null, 0, null);
        serviceProxy = new StoryServiceProxy();
    }

    @Test
    public void testGetStory_validRequest() throws IOException, TweeterRemoteException {
        StoryResponse response = serviceProxy.getStory(validRequest);
        Assertions.assertNotNull(response.getStory());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testGetStory_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.getStory(invalidRequest);
        });
    }
}
