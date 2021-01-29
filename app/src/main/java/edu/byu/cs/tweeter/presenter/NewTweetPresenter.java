package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.NewTweetServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;

public class NewTweetPresenter {

    public NewTweetPresenter() {}

    public NewTweetResponse postTweet(NewTweetRequest newTweetRequest) throws IOException, TweeterRemoteException {
        NewTweetServiceProxy newTweetServiceProxy = getNewTweetService();
        return newTweetServiceProxy.postTweet(newTweetRequest);
    }

    public NewTweetServiceProxy getNewTweetService() { return new NewTweetServiceProxy(); }
}
