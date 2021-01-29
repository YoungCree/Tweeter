package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import model.domain.Status;
import model.domain.User;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;
import server.dao.AuthTokenDAO;
import server.dao.StoryDAO;
import server.service.NewTweetServiceImpl;

public class NewTweetServiceImplTest {

    private NewTweetRequest validRequest;
    private NewTweetRequest invalidRequest;
    private NewTweetResponse successResponse;
    private NewTweetResponse failureResponse;
    private NewTweetServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("First", "Last", null);
        Status status = new Status("Hello", LocalDateTime.now(), null, null, user);

        validRequest = new NewTweetRequest(status);
        invalidRequest = new NewTweetRequest(null);

        successResponse = new NewTweetResponse();
        StoryDAO mockStoryDAO = Mockito.mock(StoryDAO.class);
        AuthTokenDAO mockAuthDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockStoryDAO.postTweet(validRequest)).thenReturn(successResponse);
        Mockito.when(mockAuthDAO.validAuth(validRequest.getStatus().getUser().getAlias())).thenReturn(true);

        failureResponse = new NewTweetResponse("An Exception occurred");
        Mockito.when(mockStoryDAO.postTweet(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new NewTweetServiceImpl());
        Mockito.when(serviceSpy.getStoryDAO()).thenReturn(mockStoryDAO);
        Mockito.when(serviceSpy.getAuthDAO()).thenReturn(mockAuthDAO);
        Mockito.doReturn(true).when(serviceSpy).updatePostQueue(validRequest);
        Mockito.doReturn(true).when(serviceSpy).updatePostQueue(invalidRequest);
    }

    @Test
    public void testNewTweet_validRequest() {
        NewTweetResponse response = serviceSpy.postTweet(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testNewTweet_invalidRequest() {
        NewTweetResponse response = serviceSpy.postTweet(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
