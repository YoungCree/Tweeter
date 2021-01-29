package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.service.response.FeedResponse;
import model.service.request.FeedRequest;
import server.service.FeedServiceImpl;

public class GetFeedHandler implements RequestHandler<FeedRequest, FeedResponse> {
    @Override
    public FeedResponse handleRequest(FeedRequest request, Context context) {
        if (request.getLimit() == 0 || request.getUser() == null)
            throw new RuntimeException("[BadRequest] rootUser or limit null");

        FeedServiceImpl service = new FeedServiceImpl();
        FeedResponse response = service.getFeed(request);
        LambdaLogger logger = context.getLogger();
        logger.log("FEED: " + response.getMessage());
        //logger.log("FEED: Status message: " + request.getLastStatus().getMessage());
        logger.log("FEED: User username: " + request.getUser().getAlias());
        logger.log("FEED: Limit val: " + request.getLimit());
        return response;
    }
}
