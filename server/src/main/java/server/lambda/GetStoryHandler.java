package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import model.service.request.StoryRequest;
import model.service.response.StoryResponse;
import server.service.StoryServiceImpl;

public class GetStoryHandler implements RequestHandler<StoryRequest, StoryResponse> {
    @Override
    public StoryResponse handleRequest(StoryRequest request, Context context) {
        if (request.getUser() == null || request.getLimit() == 0)
            throw new RuntimeException("[BadRequest] user or limit null");

        StoryServiceImpl service = new StoryServiceImpl();
        return service.getStory(request);
    }
}
