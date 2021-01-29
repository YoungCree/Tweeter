package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import server.service.FollowUserServiceImpl;
import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;

public class FollowUserHandler implements RequestHandler<FollowUserRequest, FollowUserResponse> {

    @Override
    public FollowUserResponse handleRequest(FollowUserRequest request, Context context) {
        if (request.getRootUser() == null || request.getUserToFollow() == null)
            throw new RuntimeException("[BadRequest] rootUser or userGiven null");

        FollowUserServiceImpl followUserService = new FollowUserServiceImpl();
        return followUserService.followUser(request);
    }
}
