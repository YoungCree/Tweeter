package edu.byu.cs.tweeter.model.service;

import androidx.annotation.StringRes;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class StoryServiceProxy {

    private final String URL_PATH = "/getstory";

    public StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryResponse storyResponse = getServerFacade().getStory(request, URL_PATH);

        if (storyResponse.isSuccess()) {
            loadImages(storyResponse);
            loadTimestamps(storyResponse);
        }

        return storyResponse;
    }

    private void loadImages(StoryResponse response) throws IOException {
        for (int i = 0; i < response.getStory().getSize(); i++) {
            User user = response.getStory().getStatusAt(i).getUser();
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    private void loadTimestamps(StoryResponse response) throws IOException {
        for (int i = 0; i < response.getStory().getSize(); i++) {
            if (response.getStory().getStatusAt(i).getTimestampString() != null)
                response.getStory().getStatusAt(i).setDate();
        }
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
