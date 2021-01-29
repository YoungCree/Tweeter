package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import model.domain.User;
import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;
import server.dao.UserDAO;
import server.service.UserByAliasServiceImpl;

public class UserByAliasServiceImplTest {

    private UserByAliasRequest validRequest;
    private UserByAliasRequest invalidRequest;
    private UserByAliasResponse successResponse;
    private UserByAliasResponse failureResponse;
    private UserByAliasServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        validRequest = new UserByAliasRequest("test");
        invalidRequest = new UserByAliasRequest(null);

        successResponse = new UserByAliasResponse(user);
        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDAO.getUserByAlias(validRequest.getUserAlias())).thenReturn(user);

        failureResponse = new UserByAliasResponse("An Exception occurred");
        Mockito.when(mockUserDAO.getUserByAlias(null)).thenReturn(null);

        serviceSpy = Mockito.spy(new UserByAliasServiceImpl());
        Mockito.when(serviceSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(serviceSpy.createResponse(user)).thenReturn(successResponse);
        Mockito.when(serviceSpy.createResponse(null)).thenReturn(failureResponse);
    }

    @Test
    public void testUserByAlias_validRequest() {
        UserByAliasResponse response = serviceSpy.getUserByAlias(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testUserByAlias_invalidRequest() {
        UserByAliasResponse response = serviceSpy.getUserByAlias(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
