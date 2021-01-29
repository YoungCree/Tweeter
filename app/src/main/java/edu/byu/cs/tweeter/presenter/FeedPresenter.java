package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FeedServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.FeedRequest;
import model.service.request.UserByAliasRequest;
import model.service.response.FeedResponse;

public class FeedPresenter extends FeedStoryPresenter{

    private final View view;

    public interface View {}

    public FeedPresenter(View view) {this.view = view;}

    public FeedResponse getFeed(FeedRequest feedRequest) throws IOException, TweeterRemoteException {
        FeedServiceProxy feedServiceProxy = getFeedService();
        return feedServiceProxy.getFeed(feedRequest);
    }

    FeedServiceProxy getFeedService() { return new FeedServiceProxy(); }
}
