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
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;

public class LogoutServiceProxyTest {
    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;
    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    private LogoutServiceProxy logoutServiceProxySpy;
    private String URL = "/logout";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String username = "test";
        String password = "password";

        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new LogoutRequest(user, authToken);
        invalidRequest = new LogoutRequest(null, null);

        successResponse = new LogoutResponse();
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.logout(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new LogoutResponse("An Exception occurred");
        Mockito.when(mockServerFacade.logout(invalidRequest, URL)).thenReturn(failureResponse);

        logoutServiceProxySpy = Mockito.spy(new LogoutServiceProxy());
        Mockito.when(logoutServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceProxySpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogout_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceProxySpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
