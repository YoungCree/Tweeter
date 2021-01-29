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
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;

public class SignupServiceProxyTest {

    private SignupRequest validRequest;
    private SignupRequest invalidRequest;
    private SignupResponse successResponse;
    private SignupResponse failureResponse;

    private SignupServiceProxy signupServiceProxySpy;
    private String URL = "/signup";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {

        User user = new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new SignupRequest("First", "Last", "user", "password", null);
        invalidRequest = new SignupRequest(null, null, null, null, null);

        successResponse = new SignupResponse(user, authToken);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.signup(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new SignupResponse("An Exception occurred");
        Mockito.when(mockServerFacade.signup(invalidRequest, URL)).thenReturn(failureResponse);

        signupServiceProxySpy = Mockito.spy(new SignupServiceProxy());
        Mockito.when(signupServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testSignup_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        SignupResponse response = signupServiceProxySpy.signup(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testSignup_validRequest_objectsNotNull() throws IOException, TweeterRemoteException {
        SignupResponse response = signupServiceProxySpy.signup(validRequest);
        Assertions.assertNotNull(response.getAuthToken());
        Assertions.assertNotNull(response.getUser());
    }

    @Test void testsignup_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        SignupResponse response = signupServiceProxySpy.signup(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
