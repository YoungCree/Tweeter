package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import model.domain.User;
import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;
import server.dao.AuthTokenDAO;
import server.dao.FollowingDAO;
import server.dao.UserDAO;
import server.service.FollowUserServiceImpl;

public class FollowUserServiceImplTest {

    private FollowUserRequest validRequest;
    private FollowUserRequest invalidRequest;
    private FollowUserResponse successResponse;
    private FollowUserResponse failureResponse;
    private FollowUserServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new FollowUserRequest(rootUser, userGiven);
        invalidRequest = new FollowUserRequest(rootUser, null);

        successResponse = new FollowUserResponse();
        AuthTokenDAO mockAuthDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthDAO.validAuth(validRequest.getRootUser().getAlias())).thenReturn(true);

        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);

        FollowingDAO mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.followUser(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowUserResponse("An Exception occured");
        Mockito.when(mockAuthDAO.validAuth(invalidRequest.getRootUser().getAlias())).thenReturn(true);
        Mockito.when(mockFollowingDAO.followUser(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new FollowUserServiceImpl());
        Mockito.when(serviceSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
        Mockito.when(serviceSpy.getAuthDAO()).thenReturn(mockAuthDAO);
        Mockito.when(serviceSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testGetFollowUser_validRequest() {
        FollowUserResponse response = serviceSpy.followUser(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFOllowUser_invalidRequest() {
        FollowUserResponse response = serviceSpy.followUser(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
