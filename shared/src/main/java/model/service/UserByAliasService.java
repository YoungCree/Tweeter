package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;

import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;

public interface UserByAliasService {

    public UserByAliasResponse getUserByAlias(UserByAliasRequest request) throws IOException, TweeterRemoteException;
}
