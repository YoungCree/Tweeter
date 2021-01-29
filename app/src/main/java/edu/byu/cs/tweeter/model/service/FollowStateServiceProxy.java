package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.FollowStateService;
import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;

public class FollowStateServiceProxy implements FollowStateService {

    private final String URL_PATH = "/getfollowstate";

    public FollowStateResponse followState(FollowStateRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        FollowStateResponse response = serverFacade.followState(request, URL_PATH);

        return response;
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
