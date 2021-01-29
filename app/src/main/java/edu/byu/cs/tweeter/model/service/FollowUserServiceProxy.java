package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.FollowUserService;
import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;

public class FollowUserServiceProxy implements FollowUserService {

    private final String URL_PATH = "/followuser";

    public FollowUserResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        FollowUserResponse response = serverFacade.followUser(request, URL_PATH);

        return response;
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
