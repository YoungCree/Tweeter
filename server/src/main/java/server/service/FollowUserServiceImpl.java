package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.FollowUserService;
import server.dao.AuthTokenDAO;
import server.dao.FollowingDAO;
import model.service.response.FollowUserResponse;
import model.service.request.FollowUserRequest;
import server.dao.UserDAO;

public class FollowUserServiceImpl implements FollowUserService {
    @Override
    public FollowUserResponse followUser(FollowUserRequest request) {
        if (!getAuthDAO().validAuth(request.getRootUser().getAlias())) {
            return new FollowUserResponse("Your session has expired, please log back in");
        }
        if (request.getUserToFollow() != null && request.getRootUser() != null) {
            getUserDAO().updateNumFollow(request.getRootUser().getAlias(),
                    request.getUserToFollow().getAlias(), true);
        }
        return getFollowingDAO().followUser(request);
    }

    public FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
    public UserDAO getUserDAO() { return new UserDAO(); }
    public AuthTokenDAO getAuthDAO() { return new AuthTokenDAO(); }
}
