package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;

public interface NumFollowersFollowingService {

    public NumFollowersFollowingResponse
    getNumFollowersFollowing(NumFollowersFollowingRequest request)
            throws IOException, TweeterRemoteException;
}
