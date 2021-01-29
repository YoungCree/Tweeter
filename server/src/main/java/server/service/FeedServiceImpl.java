package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.FeedService;
import model.service.response.FeedResponse;
import model.service.request.FeedRequest;
import server.dao.FeedDAO;

public class FeedServiceImpl implements FeedService {
    @Override
    public FeedResponse getFeed(FeedRequest request) {
        return getFeedDAO().getFeed(request);
    }

    public FeedDAO getFeedDAO() { return new FeedDAO(); }
}
