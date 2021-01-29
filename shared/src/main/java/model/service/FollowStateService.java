package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;

import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;

public interface FollowStateService {

    public FollowStateResponse followState(FollowStateRequest request) throws IOException, TweeterRemoteException;
}
