package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;

public class UserByAliasServiceProxyTest {

    private UserByAliasRequest validRequest;
    private UserByAliasRequest invalidRequest;
    private UserByAliasResponse successResponse;
    private UserByAliasResponse failureResponse;

    private UserByAliasServiceProxy userByAliasServiceProxySpy;
    private String URL = "/getuserbyalias";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        validRequest = new UserByAliasRequest("test");
        invalidRequest = new UserByAliasRequest(null);

        successResponse = new UserByAliasResponse(user);
        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getUserByAlias(validRequest, URL)).thenReturn(successResponse);

        failureResponse = new UserByAliasResponse("An Exception occurred");
        Mockito.when(mockServerFacade.getUserByAlias(invalidRequest, URL)).thenReturn(failureResponse);

        userByAliasServiceProxySpy = Mockito.spy(new UserByAliasServiceProxy());
        Mockito.when(userByAliasServiceProxySpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testUserByAlias_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        UserByAliasResponse response = userByAliasServiceProxySpy.getUserByAlias(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testUserByAlias_validRequest_objectsNotNull() throws IOException, TweeterRemoteException {
        UserByAliasResponse response = userByAliasServiceProxySpy.getUserByAlias(validRequest);
        Assertions.assertNotNull(response.getUser());
        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testUserByAlias_invalidRequest_returnsNothing() throws IOException, TweeterRemoteException {
        UserByAliasResponse response = userByAliasServiceProxySpy.getUserByAlias(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
