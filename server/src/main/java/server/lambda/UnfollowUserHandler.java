package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import server.service.UnfollowUserServiceImpl;
import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;

public class UnfollowUserHandler implements RequestHandler<UnfollowUserRequest, UnfollowUserResponse> {

    @Override
    public UnfollowUserResponse handleRequest(UnfollowUserRequest request, Context context) {
        if (request.getRootUser() == null || request.getUserToUnfollow() == null)
            throw new RuntimeException("[BadRequest] rootUser or userToUnfollow null");

        UnfollowUserServiceImpl service = new UnfollowUserServiceImpl();
        return service.unfollowUser(request);
    }
}
