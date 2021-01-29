package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;

import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;

public interface FollowUserService {

    FollowUserResponse followUser(FollowUserRequest request) throws IOException, TweeterRemoteException;
}
