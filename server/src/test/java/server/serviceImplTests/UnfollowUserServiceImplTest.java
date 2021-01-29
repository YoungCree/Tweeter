package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import model.domain.User;
import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;
import server.dao.AuthTokenDAO;
import server.dao.FollowingDAO;
import server.dao.UserDAO;
import server.service.UnfollowUserServiceImpl;

public class UnfollowUserServiceImplTest {

    private UnfollowUserRequest validRequest;
    private UnfollowUserRequest invalidRequest;
    private UnfollowUserResponse successResponse;
    private UnfollowUserResponse failureResponse;
    private UnfollowUserServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new UnfollowUserRequest(rootUser, userGiven);
        invalidRequest = new UnfollowUserRequest(null, null);

        successResponse = new UnfollowUserResponse();
        FollowingDAO mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);
        AuthTokenDAO mockAuthDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockFollowingDAO.unfollowUser(validRequest)).thenReturn(successResponse);
        Mockito.when(mockAuthDAO.validAuth(validRequest.getRootUser().getAlias())).thenReturn(true);

        failureResponse = new UnfollowUserResponse("An Exception occured");
        Mockito.when(mockFollowingDAO.unfollowUser(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new UnfollowUserServiceImpl());
        Mockito.when(serviceSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
        Mockito.when(serviceSpy.getAuthDAO()).thenReturn(mockAuthDAO);
        Mockito.when(serviceSpy.getUserDAO()).thenReturn(mockUserDAO);
    }

    @Test
    public void testGetUnfollowUser_validRequest() {
        UnfollowUserResponse response = serviceSpy.unfollowUser(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetUnfollowUser_invalidRequest() {
        UnfollowUserResponse response = serviceSpy.unfollowUser(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
