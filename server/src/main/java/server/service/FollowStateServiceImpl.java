package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.FollowStateService;
import model.service.response.FollowStateResponse;
import model.service.request.FollowStateRequest;
import server.dao.FollowingDAO;

public class FollowStateServiceImpl implements FollowStateService {

    @Override
    public FollowStateResponse followState(FollowStateRequest request) {
        return getFollowingDAO().followState(request);
    }

    public FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
}
