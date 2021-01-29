package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;

import model.domain.Status;
import model.domain.Story;
import model.domain.User;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;
import server.dao.StoryDAO;
import server.service.StoryServiceImpl;

public class StoryServiceImplTest {

    private StoryRequest validRequest;
    private StoryRequest invalidRequest;
    private StoryResponse successResponse;
    private StoryResponse failureResponse;
    private StoryServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status resultStatus1 = new Status("test", LocalDateTime.now(), null, null, currentUser);
        Status resultStatus2 = new Status("test2", LocalDateTime.now().minusDays(1), null, null, currentUser);
        Status resultStatus3 = new Status("test3", LocalDateTime.now().minusHours(3), null, null, currentUser);

        validRequest = new StoryRequest(currentUser, 3, null);
        invalidRequest = new StoryRequest(null, 0, null);

        successResponse = new StoryResponse(new Story(Arrays.asList(resultStatus1, resultStatus2, resultStatus3)), false);
        StoryDAO mockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mockStoryDAO.getStory(validRequest)).thenReturn(successResponse);

        failureResponse = new StoryResponse("An exception occured");
        Mockito.when(mockStoryDAO.getStory(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new StoryServiceImpl());
        Mockito.when(serviceSpy.getStoryDAO()).thenReturn(mockStoryDAO);
    }

    @Test
    public void testGetStory_validRequest() {
        StoryResponse response = serviceSpy.getStory(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetStory_invalidRequest() {
        StoryResponse response = serviceSpy.getStory(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
