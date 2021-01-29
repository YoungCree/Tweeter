package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.UnfollowUserService;
import model.service.response.UnfollowUserResponse;
import model.service.request.UnfollowUserRequest;
import server.dao.AuthTokenDAO;
import server.dao.FollowingDAO;
import server.dao.UserDAO;

public class UnfollowUserServiceImpl implements UnfollowUserService {
    @Override
    public UnfollowUserResponse unfollowUser(UnfollowUserRequest request) {
        if (request.getRootUser() != null) {
            if (!getAuthDAO().validAuth(request.getRootUser().getAlias())) {
                return new UnfollowUserResponse("Your session has expired, please log back in");
            }

            getUserDAO().updateNumFollow(request.getRootUser().getAlias(),
                    request.getUserToUnfollow().getAlias(), false);
        }
        return getFollowingDAO().unfollowUser(request);
    }

    public FollowingDAO getFollowingDAO() { return new FollowingDAO(); }
    public UserDAO getUserDAO() { return new UserDAO(); }
    public AuthTokenDAO getAuthDAO() { return new AuthTokenDAO(); }
}
