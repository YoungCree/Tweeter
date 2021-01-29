package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.response.FeedResponse;
import model.service.request.FeedRequest;

public interface FeedService {

    FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException;
}
