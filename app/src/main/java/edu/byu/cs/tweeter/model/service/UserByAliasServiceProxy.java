package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.UserByAliasService;
import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class UserByAliasServiceProxy implements UserByAliasService {

    private final String URL_PATH = "/getuserbyalias";

    public UserByAliasResponse getUserByAlias(UserByAliasRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        UserByAliasResponse userByAliasResponse = serverFacade.getUserByAlias(request, URL_PATH);

        if (userByAliasResponse.isSuccess()) {
            loadImage(userByAliasResponse.getUser());
        }

        return userByAliasResponse;
    }

    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
