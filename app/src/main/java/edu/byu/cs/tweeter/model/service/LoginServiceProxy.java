package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.LoginService;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

/**
 * Contains the business logic to support the login operation.
 */
public class LoginServiceProxy implements LoginService {

    static final String URL_PATH = "/login";

    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        LoginResponse loginResponse = serverFacade.login(request, URL_PATH);

        if(loginResponse.isSuccess()) {
            loadImage(loginResponse.getUser());
        }

        return loginResponse;
    }

    /**
     * Loads the profile image data for the user.
     *
     * @param user the user whose profile image data is to be loaded.
     */
    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
