package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.StoryServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.StoryRequest;
import model.service.request.UserByAliasRequest;
import model.service.response.StoryResponse;

public class StoryPresenter extends FeedStoryPresenter {

    private final View view;

    public interface View {}

    public StoryPresenter(View view) { this.view = view; }

    public StoryResponse getStory(StoryRequest storyRequest) throws IOException, TweeterRemoteException {
        StoryServiceProxy storyServiceProxy = getStoryService();
        return storyServiceProxy.getStory(storyRequest);
    }

    public StoryServiceProxy getStoryService() { return new StoryServiceProxy(); }
}
