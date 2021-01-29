package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.response.UnfollowUserResponse;
import model.service.request.UnfollowUserRequest;

public interface UnfollowUserService {

    UnfollowUserResponse unfollowUser(UnfollowUserRequest request) throws IOException, TweeterRemoteException;
}
