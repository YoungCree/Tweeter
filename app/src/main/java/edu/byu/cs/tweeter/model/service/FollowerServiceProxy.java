package edu.byu.cs.tweeter.model.service;

import java.io.IOException;
import java.net.URL;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.FollowerService;
import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class FollowerServiceProxy implements FollowerService {

    static final String URL_PATH = "/getfollowers";

    public FollowerResponse getFollowers(FollowerRequest request) throws IOException, TweeterRemoteException {
        FollowerResponse response = getServerFacade().getFollowers(request, URL_PATH);

        if (response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    private void loadImages(FollowerResponse response) throws IOException {
        for (User user : response.getFollowers()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
