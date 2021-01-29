package server.service;

import java.io.IOException;

import javax.jws.soap.SOAPBinding;

import model.net.TweeterRemoteException;
import model.service.NumFollowersFollowingService;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;
import server.dao.FollowingDAO;
import server.dao.UserDAO;

public class NumFollowersFollowingServiceImpl implements NumFollowersFollowingService {

    @Override
    public NumFollowersFollowingResponse
    getNumFollowersFollowing(NumFollowersFollowingRequest request) {
        return getUserDAO().getNumFollow(request);
    }

    public FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
    public UserDAO getUserDAO() { return new UserDAO(); }
}
