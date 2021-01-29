package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;

import model.domain.Feed;
import model.domain.Status;
import model.domain.User;
import model.service.request.FeedRequest;
import model.service.response.FeedResponse;
import server.dao.FeedDAO;
import server.service.FeedServiceImpl;

public class FeedServiceImplTest {

    private FeedRequest validRequest;
    private FeedRequest invalidRequest;
    private FeedResponse successResponse;
    private FeedResponse failureResponse;
    private FeedServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status resultStatus1 = new Status("test", LocalDateTime.now(), null, null, currentUser);
        Status resultStatus2 = new Status("test2", LocalDateTime.now().minusDays(1), null, null, currentUser);
        Status resultStatus3 = new Status("test3", LocalDateTime.now().minusHours(3), null, null, currentUser);

        validRequest = new FeedRequest(currentUser, 3, null);
        invalidRequest = new FeedRequest(null, 0, null);

        successResponse = new FeedResponse(new Feed(Arrays.asList(resultStatus1, resultStatus2, resultStatus3)), false);
        FeedDAO mockDAO = Mockito.mock(FeedDAO.class);
        Mockito.when(mockDAO.getFeed(validRequest)).thenReturn(successResponse);

        failureResponse = new FeedResponse("An exception occurred");
        Mockito.when(mockDAO.getFeed(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new FeedServiceImpl());
        Mockito.when(serviceSpy.getFeedDAO()).thenReturn(mockDAO);
    }

    @Test
    public void testGetFeed_validRequest() {
        FeedResponse response = serviceSpy.getFeed(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFeed_invalidRequest() {
        FeedResponse response = serviceSpy.getFeed(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
