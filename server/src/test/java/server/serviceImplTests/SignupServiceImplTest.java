package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.User;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;
import server.dao.AuthTokenDAO;
import server.dao.S3DAO;
import server.dao.UserDAO;
import server.service.SignupServiceImpl;

public class SignupServiceImplTest {

    private SignupRequest validRequest;
    private SignupRequest invalidRequest;
    private SignupResponse successResponse;
    private SignupResponse failureResponse;
    private SignupServiceImpl serviceSpy;

    @BeforeEach
    public void setup() throws IOException {
        User user = new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new SignupRequest("First", "Last", "user", "password", null);
        invalidRequest = new SignupRequest(null, null, null, null, null);

        successResponse = new SignupResponse(user, authToken);
        UserDAO mockUserDAO = Mockito.mock(UserDAO.class);
        AuthTokenDAO mockAuthDAO = Mockito.mock(AuthTokenDAO.class);
        S3DAO mockS3DAO = Mockito.mock(S3DAO.class);
        Mockito.when(mockUserDAO.createUser(validRequest, null)).thenReturn(user);
        Mockito.when(mockAuthDAO.getAuthToken(validRequest)).thenReturn(authToken);
        Mockito.when(mockS3DAO.saveImage(null, validRequest.getUsername())).thenReturn(null);

        failureResponse = new SignupResponse("An Exception occurred");
        Mockito.when(mockUserDAO.createUser(invalidRequest, "user")).thenReturn(null);
        Mockito.when(mockAuthDAO.getAuthToken(invalidRequest)).thenReturn(null);

        serviceSpy = Mockito.spy(new SignupServiceImpl());
        Mockito.when(serviceSpy.getUserDAO()).thenReturn(mockUserDAO);
        Mockito.when(serviceSpy.getAuthTokenDAO()).thenReturn(mockAuthDAO);
        Mockito.when(serviceSpy.createResponse(user, authToken)).thenReturn(successResponse);
        Mockito.when(serviceSpy.createResponse(null, null)).thenReturn(failureResponse);
        Mockito.when(serviceSpy.getS3DAO()).thenReturn(mockS3DAO);
    }

    @Test
    public void testSignup_validRequest() throws IOException {
        SignupResponse response = serviceSpy.signup(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testSignup_invalidRequest() throws IOException {
        SignupResponse response = serviceSpy.signup(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
