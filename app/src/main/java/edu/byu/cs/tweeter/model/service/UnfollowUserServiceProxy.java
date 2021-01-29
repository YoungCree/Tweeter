package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.UnfollowUserService;
import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;

public class UnfollowUserServiceProxy implements UnfollowUserService {

    private final String URL_PATH = "/unfollowuser";

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        UnfollowUserResponse response = serverFacade.unfollowUser(request, URL_PATH);

        return response;
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
