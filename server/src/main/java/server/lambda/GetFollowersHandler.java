package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;
import server.service.FollowerServiceImpl;

public class GetFollowersHandler implements RequestHandler<FollowerRequest, FollowerResponse> {

    @Override
    public FollowerResponse handleRequest(FollowerRequest followerRequest, Context context) {
        if (followerRequest.getFollowee() == null || followerRequest.getLimit() == 0)
            throw new RuntimeException("[BadRequest] user or limit null");

        FollowerServiceImpl service = new FollowerServiceImpl();
        return service.getFollowers(followerRequest);
    }
}
