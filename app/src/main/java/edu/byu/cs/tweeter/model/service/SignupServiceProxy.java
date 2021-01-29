package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class SignupServiceProxy {

    private final String URL_PATH = "/signup";

    public SignupResponse signup(SignupRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        SignupResponse signupResponse = serverFacade.signup(request, URL_PATH);

        if (signupResponse.isSuccess()) {
            loadImage(signupResponse.getUser());
        }

        return signupResponse;
    }

    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
