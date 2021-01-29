package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.SignupServiceProxy;
import edu.byu.cs.tweeter.util.ByteArrayUtils;
import model.domain.AuthToken;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;

public class SignupTest {

    private SignupRequest validRequest;
    private SignupRequest invalidRequest;
    private SignupServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        ByteArrayUtils helper = new ByteArrayUtils();
        validRequest = new SignupRequest("First", "Last", "@FirstNameLastName", "password", helper.bytesFromUrl("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png"));
        invalidRequest = new SignupRequest(null, null, null, null, null);
        serviceProxy = new SignupServiceProxy();
    }

    @Test
    public void testSignup_validRequest() throws IOException, TweeterRemoteException {
        SignupResponse response = serviceProxy.signup(validRequest);
        Assertions.assertNotNull(response.getAuthToken());
        Assertions.assertNotNull(response.getUser());
    }

    @Test
    public void testSignup_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.signup(invalidRequest);
        });
    }
}
