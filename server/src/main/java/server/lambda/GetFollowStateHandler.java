package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import server.service.FollowStateServiceImpl;
import model.service.response.FollowStateResponse;
import model.service.request.FollowStateRequest;

public class GetFollowStateHandler implements RequestHandler<FollowStateRequest, FollowStateResponse> {
    @Override
    public FollowStateResponse handleRequest(FollowStateRequest request, Context context) {
        if (request.getRootUser() == null || request.getUserGiven() == null)
            throw new RuntimeException("[BadRequest] rootUser or userGiven null");

        FollowStateServiceImpl service = new FollowStateServiceImpl();
        LambdaLogger logger = context.getLogger();
        FollowStateResponse response = service.followState(request);
        if (response.getIsFollowing()) logger.log("following");
        else logger.log("not following");
        return response;
    }
}
