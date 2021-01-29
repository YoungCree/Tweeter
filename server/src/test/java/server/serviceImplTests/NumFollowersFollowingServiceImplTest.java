package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import model.domain.User;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;
import server.dao.FollowingDAO;
import server.dao.UserDAO;
import server.service.NumFollowersFollowingServiceImpl;

public class NumFollowersFollowingServiceImplTest {

    private NumFollowersFollowingRequest validRequest;
    private NumFollowersFollowingRequest invalidRequest;
    private NumFollowersFollowingResponse successResponse;
    private NumFollowersFollowingResponse failureResponse;
    private NumFollowersFollowingServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("First", "Last", null);

        validRequest = new NumFollowersFollowingRequest(user);
        invalidRequest = new NumFollowersFollowingRequest(null);

        successResponse = new NumFollowersFollowingResponse(4, 5);
        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getNumFollow(validRequest)).thenReturn(successResponse);

        failureResponse = new NumFollowersFollowingResponse("An Exception Occurred");
        Mockito.when(mockUserDAO.getNumFollow(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new NumFollowersFollowingServiceImpl());
        Mockito.when(serviceSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testNumFolFol_validRequest() {
        NumFollowersFollowingResponse response = serviceSpy.getNumFollowersFollowing(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testNumFolFol_invalidRequest() {
        NumFollowersFollowingResponse response = serviceSpy.getNumFollowersFollowing(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
