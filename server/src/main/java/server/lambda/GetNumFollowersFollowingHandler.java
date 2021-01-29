package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;
import server.service.NumFollowersFollowingServiceImpl;

public class GetNumFollowersFollowingHandler implements RequestHandler<NumFollowersFollowingRequest, NumFollowersFollowingResponse> {
    @Override
    public NumFollowersFollowingResponse handleRequest(NumFollowersFollowingRequest request, Context context) {
        if (request.getUser() == null)
            throw new RuntimeException("[BadRequest] user null");

        NumFollowersFollowingServiceImpl service = new NumFollowersFollowingServiceImpl();
        return service.getNumFollowersFollowing(request);
    }
}
