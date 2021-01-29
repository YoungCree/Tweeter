package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;
import server.service.NewTweetServiceImpl;

public class PostTweetHandler implements RequestHandler<NewTweetRequest, NewTweetResponse> {

    @Override
    public NewTweetResponse handleRequest(NewTweetRequest newTweetRequest, Context context) {
        if (newTweetRequest.getStatus() == null)
            throw new RuntimeException("[BadRequest] status null");

        NewTweetServiceImpl service = new NewTweetServiceImpl();
        return service.postTweet(newTweetRequest);
    }
}
