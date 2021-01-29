package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.FeedService;
import model.service.request.FeedRequest;
import model.service.response.FeedResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class FeedServiceProxy implements FeedService {

    static final String URL_PATH = "/getfeed";

    public FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        FeedResponse feedResponse = getServerFacade().getFeed(request, URL_PATH);

        if (feedResponse.isSuccess()) {
            loadImages(feedResponse);
            loadTimestamp(feedResponse);
        }

        return feedResponse;
    }

    private void loadImages(FeedResponse response) throws IOException {
        for (int i = 0; i < response.getFeed().getSize(); i++) {
            User user = response.getFeed().getStatusAt(i).getUser();
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    private void loadTimestamp(FeedResponse response) throws IOException {
        for (int i = 0; i < response.getFeed().getSize(); i++) {
            if (response.getFeed().getStatusAt(i).getTimestampString() != null)
                response.getFeed().getStatusAt(i).setDate();
        }
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
