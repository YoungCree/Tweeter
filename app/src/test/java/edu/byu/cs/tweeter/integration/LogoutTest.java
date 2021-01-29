package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LogoutServiceProxy;
import model.domain.AuthToken;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;

public class LogoutTest {
    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;
    private LogoutServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new LogoutRequest(user, authToken);
        invalidRequest = new LogoutRequest(null, null);
        serviceProxy = new LogoutServiceProxy();
    }

    @Test
    public void testLogout_validRequest() throws IOException, TweeterRemoteException {
        LogoutResponse response = serviceProxy.logout(validRequest);
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testLogout_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.logout(invalidRequest);
        });
    }
}
