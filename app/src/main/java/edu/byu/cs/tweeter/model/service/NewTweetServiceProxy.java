package edu.byu.cs.tweeter.model.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;

public class NewTweetServiceProxy {

    private final String URL_PATH = "/posttweet";

    public NewTweetResponse postTweet(NewTweetRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        if (request.getStatus() != null && request.getStatus().getTimestamp() != null) {
            request.getStatus().setTimestampString(request.getStatus().getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        NewTweetResponse newTweetResponse = serverFacade.postTweet(request, URL_PATH);

        return newTweetResponse;
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
