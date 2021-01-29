package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LoginServiceProxy;
import model.domain.AuthToken;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;
import model.service.response.LogoutResponse;

public class LoginTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;
    private LoginServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String username = "@Guy1";
        String password = "easy123";

        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new LoginRequest(username, password);
        invalidRequest = new LoginRequest(null, null);
        serviceProxy = new LoginServiceProxy();
    }

    @Test
    public void testLogin_validRequest() throws IOException, TweeterRemoteException {
        LoginResponse response = serviceProxy.login(validRequest);
        Assertions.assertNotNull(response.getAuthToken());
        Assertions.assertNotNull(response.getUser());
    }

    @Test
    public void testLogin_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.login(invalidRequest);
        });
    }
}
