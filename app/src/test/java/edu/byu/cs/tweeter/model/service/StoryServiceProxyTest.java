package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import model.domain.Status;
import model.domain.Story;
import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;

public class StoryServiceProxyTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;
    private StoryResponse successResponse;
    private StoryResponse failureResponse;

    private StoryServiceProxy feedServiceSpy;
    private String URL = "/getstory";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status resultStatus1 = new Status("test", LocalDateTime.now(), null, null, currentUser);
        Status resultStatus2 = new Status("test2", LocalDateTime.now().minusDays(1), null, null, currentUser);
        Status resultStatus3 = new Status("test3", LocalDateTime.now().minusHours(3), null, null, currentUser);

        validRequest = new StoryRequest(currentUser, 3, null);
        invalidRequest = new StoryRequest(null, 0, null);

        successResponse = new StoryResponse(new Story(Arrays.asList(resultStatus1, resultStatus2, resultStatus3)), false);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStory(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new StoryResponse("An exception occured");
        Mockito.when(mockServerFacade.getStory(invalidRequest, URL)).thenReturn(failureResponse);

        feedServiceSpy = Mockito.spy(new StoryServiceProxy());
        Mockito.when(feedServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = feedServiceSpy.getStory(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetStory_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        StoryResponse response = feedServiceSpy.getStory(validRequest);

        for (int i = 0; i < response.getStory().getSize(); i++) {
            User tmpUser = response.getStory().getStatusAt(i).getUser();
            Assertions.assertNotNull(tmpUser.getImageBytes());
        }
    }

    @Test
    public void testGetStory_invalidRequest_returnsNoStory() throws IOException, TweeterRemoteException {
        StoryResponse response = feedServiceSpy.getStory(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
