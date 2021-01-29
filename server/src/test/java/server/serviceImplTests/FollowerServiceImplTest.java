package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import model.domain.User;
import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;
import server.dao.FollowingDAO;
import server.service.FollowerServiceImpl;

public class FollowerServiceImplTest {

    private FollowerRequest validRequest;
    private FollowerRequest invalidRequest;
    private FollowerResponse successResponse;
    private FollowerResponse failureResponse;
    private FollowerServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        validRequest = new FollowerRequest(currentUser, 3, null);
        invalidRequest = new FollowerRequest(null, 0, null);

        successResponse = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        FollowingDAO mockDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockDAO.getFollowers(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowerResponse("An exception occurred");
        Mockito.when(mockDAO.getFollowers(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new FollowerServiceImpl());
        Mockito.when(serviceSpy.getFollowingDAO()).thenReturn(mockDAO);
    }

    @Test
    public void testGetFollowers_validRequest() {
        FollowerResponse response = serviceSpy.getFollowers(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowers_invalidRequest() {
        FollowerResponse response = serviceSpy.getFollowers(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
