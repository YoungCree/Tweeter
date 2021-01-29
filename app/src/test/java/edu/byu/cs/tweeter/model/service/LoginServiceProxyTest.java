package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;

public class LoginServiceProxyTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;
    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceProxy loginServiceProxySpy;
    private String URL = "/login";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String username = "test";
        String password = "password";

        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new LoginRequest(username, password);
        invalidRequest = new LoginRequest(null, null);

        successResponse = new LoginResponse(user, authToken);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.login(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new LoginResponse("An Exception occurred");
        Mockito.when(mockServerFacade.login(invalidRequest, URL)).thenReturn(failureResponse);

        loginServiceProxySpy = Mockito.spy(new LoginServiceProxy());
        Mockito.when(loginServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceProxySpy.login(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogin_validRequest_objectsNotNull() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceProxySpy.login(validRequest);
        Assertions.assertNotNull(response.getAuthToken());
        Assertions.assertNotNull(response.getUser());
    }

    @Test
    public void testLogin_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceProxySpy.login(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
