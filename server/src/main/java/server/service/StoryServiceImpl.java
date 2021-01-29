package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.StoryService;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;
import server.dao.StoryDAO;

public class StoryServiceImpl implements StoryService {
    @Override
    public StoryResponse getStory(StoryRequest request) {
        return getStoryDAO().getStory(request);
    }

    public StoryDAO getStoryDAO() { return new StoryDAO(); }
}
