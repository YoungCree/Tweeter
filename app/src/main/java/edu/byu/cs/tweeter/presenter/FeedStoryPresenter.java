package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.UserByAliasServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;

public abstract class FeedStoryPresenter {

    public UserByAliasResponse getUserByAlias(UserByAliasRequest request) throws IOException, TweeterRemoteException {
        UserByAliasServiceProxy userByAliasServiceProxy = new UserByAliasServiceProxy();
        return userByAliasServiceProxy.getUserByAlias(request);
    }
}
