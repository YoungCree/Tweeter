package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowerServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;

public class FollowerPresenter {

    private final View view;

    public interface View {}

    public FollowerPresenter(View view) { this.view = view; }

    public FollowerResponse getFollower(FollowerRequest request) throws IOException, TweeterRemoteException {
        FollowerServiceProxy followerServiceProxy = getFollowerService();
        return followerServiceProxy.getFollowers(request);
    }

    FollowerServiceProxy getFollowerService() { return new FollowerServiceProxy(); }
}
