package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import model.domain.User;
import model.service.request.FollowingRequest;
import model.service.response.FollowingResponse;
import server.dao.FollowingDAO;
import server.service.FollowingServiceImpl;

public class FollowingServiceImplTest {

    private FollowingRequest validRequest;
    private FollowingRequest invalidRequest;

    private FollowingResponse successResponse;
    private FollowingResponse failureResponse;

    private FollowingServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowingRequest(currentUser, 3, null);
        invalidRequest = new FollowingRequest(null, 0, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        FollowingDAO mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.getFollowees(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowingResponse("An exception occurred");
        Mockito.when(mockFollowingDAO.getFollowees(invalidRequest)).thenReturn(failureResponse);

        // Create a FollowingServiceProxy instance and wrap it with a spy that will use the mock service
        serviceSpy = Mockito.spy(new FollowingServiceImpl());
        Mockito.when(serviceSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
    }

    @Test
    public void testGetFollowees_validRequest() {
        FollowingResponse response = serviceSpy.getFollowees(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowees_invalidRequest() {
        FollowingResponse response = serviceSpy.getFollowees(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
