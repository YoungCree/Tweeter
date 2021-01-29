package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;

public interface FollowerService {

    FollowerResponse getFollowers(FollowerRequest request)
            throws IOException, TweeterRemoteException;
}
