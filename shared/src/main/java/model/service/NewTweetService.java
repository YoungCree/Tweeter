package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;

public interface NewTweetService {

    NewTweetResponse postTweet(NewTweetRequest request) throws IOException, TweeterRemoteException;
}
