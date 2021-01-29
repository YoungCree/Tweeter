package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import model.domain.AuthToken;
import model.domain.User;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;
import server.dao.AuthTokenDAO;
import server.dao.UserDAO;
import server.helper.Hasher;
import server.service.LoginServiceImpl;
import server.service.LogoutServiceImpl;

public class LoginServiceImplTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;
    private LoginResponse successResponse;
    private LoginResponse failureResponse;
    private LoginServiceImpl serviceSpy;

    @BeforeEach
    public void setup() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username = "test";
        String password = "password";

        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new LoginRequest(username, password);
        invalidRequest = new LoginRequest(null, null);

        successResponse = new LoginResponse(user, authToken);
        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);
        Hasher mockHasher = Mockito.mock(Hasher.class);
        AuthTokenDAO mockAuthDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockUserDAO.getUser(validRequest.getUsername())).thenReturn(user);
        Mockito.when(mockHasher.validatePassword(validRequest.getPassword(), user.getPassword())).thenReturn(true);
        Mockito.when(mockAuthDAO.getAuthToken(validRequest)).thenReturn(authToken);

        failureResponse = new LoginResponse("An Exception occurred");
        Mockito.when(mockUserDAO.getUser(null)).thenReturn(null);
        Mockito.when(mockAuthDAO.getAuthToken(invalidRequest)).thenReturn(null);

        serviceSpy = Mockito.spy(new LoginServiceImpl());
        Mockito.when(serviceSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(serviceSpy.getAuthTokenDAO()).thenReturn(mockAuthDAO);
        Mockito.when(serviceSpy.getHasher()).thenReturn(mockHasher);
        Mockito.when(serviceSpy.createResponse(user, authToken)).thenReturn(successResponse);
        Mockito.when(serviceSpy.createResponse(null, null)).thenReturn(failureResponse);
    }

    @Test
    public void testLogin_validRequest() {
        LoginResponse response = serviceSpy.login(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogin_invalidRequest() {
        LoginResponse response = serviceSpy.login(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
