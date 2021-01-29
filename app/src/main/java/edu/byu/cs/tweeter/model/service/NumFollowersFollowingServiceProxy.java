package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.NumFollowersFollowingService;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;

public class NumFollowersFollowingServiceProxy implements NumFollowersFollowingService {

    private final String URL_PATH = "/getnumfolfol";

    public NumFollowersFollowingResponse
    getNumFollowersFollowing(NumFollowersFollowingRequest request) throws IOException, TweeterRemoteException {
        NumFollowersFollowingResponse numFollowersFollowingResponse = getServerFacade().getNumFollowersFollowing(request, URL_PATH);

        return numFollowersFollowingResponse;
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
