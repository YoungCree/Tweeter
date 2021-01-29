package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.FollowerService;
import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;
import server.dao.FollowingDAO;

public class FollowerServiceImpl implements FollowerService {
    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        return getFollowingDAO().getFollowers(request);
    }

    public FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
}
