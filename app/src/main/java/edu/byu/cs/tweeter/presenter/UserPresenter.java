package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.FollowStateServiceProxy;
import edu.byu.cs.tweeter.model.service.FollowUserServiceProxy;
import edu.byu.cs.tweeter.model.service.UnfollowUserServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.FollowStateRequest;
import model.service.request.FollowUserRequest;
import model.service.request.UnfollowUserRequest;
import model.service.response.FollowStateResponse;
import model.service.response.FollowUserResponse;
import model.service.response.UnfollowUserResponse;

public class UserPresenter extends UserMainPresenter {

    public UserPresenter() {}

    public FollowUserResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException {
        FollowUserServiceProxy followUserServiceProxy = getFollowUserService();
        return followUserServiceProxy.followUser(request);
    }

    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException, TweeterRemoteException {
        UnfollowUserServiceProxy unfollowUserServiceProxy = getUnfollowUserService();
        return unfollowUserServiceProxy.unfollowUser(request);
    }

    public FollowStateResponse followState(FollowStateRequest request) throws IOException, TweeterRemoteException {
        FollowStateServiceProxy followStateServiceProxy = getFollowStateService();
        return followStateServiceProxy.followState(request);
    }

    public FollowUserServiceProxy getFollowUserService() { return new FollowUserServiceProxy(); }

    public UnfollowUserServiceProxy getUnfollowUserService() { return new UnfollowUserServiceProxy(); }

    public FollowStateServiceProxy getFollowStateService() { return new FollowStateServiceProxy(); }
}
