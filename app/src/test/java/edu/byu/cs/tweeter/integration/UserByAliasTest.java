package edu.byu.cs.tweeter.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.UserByAliasServiceProxy;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;

public class UserByAliasTest {

    private UserByAliasRequest validRequest;
    private UserByAliasRequest invalidRequest;
    private UserByAliasServiceProxy serviceProxy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        validRequest = new UserByAliasRequest("test");
        invalidRequest = new UserByAliasRequest(null);
        serviceProxy = new UserByAliasServiceProxy();
    }

    @Test
    public void testUserByAlias_validRequest() throws IOException, TweeterRemoteException {
        UserByAliasResponse response = serviceProxy.getUserByAlias(validRequest);
        Assertions.assertNotNull(response.getUser());
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    public void testUserByAlias_invalidRequest() {
        Assertions.assertThrows(TweeterRemoteException.class, () -> {
            serviceProxy.getUserByAlias(invalidRequest);
        });
    }
}
