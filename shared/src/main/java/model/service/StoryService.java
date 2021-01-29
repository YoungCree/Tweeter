package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;

public interface StoryService {

    StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException;
}
