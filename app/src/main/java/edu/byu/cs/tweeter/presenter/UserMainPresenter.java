package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.NumFollowersFollowingServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;

public abstract class UserMainPresenter {

    public NumFollowersFollowingResponse
    getNumFollowersFollowing(NumFollowersFollowingRequest request)
            throws IOException, TweeterRemoteException {
        NumFollowersFollowingServiceProxy numFollowersFollowingServiceProxy = getNumFollowersFollowingService();
        return numFollowersFollowingServiceProxy.getNumFollowersFollowing(request);
    }

    public NumFollowersFollowingServiceProxy getNumFollowersFollowingService() { return new NumFollowersFollowingServiceProxy(); }
}
