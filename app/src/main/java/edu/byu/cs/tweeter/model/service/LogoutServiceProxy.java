package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.ServerFacade;
import model.net.TweeterRemoteException;
import model.service.LogoutService;
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;

public class LogoutServiceProxy implements LogoutService {

    private final String URL_PATH = "/logout";

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        LogoutResponse response = serverFacade.logout(request, URL_PATH);

        return response;
    }

    ServerFacade getServerFacade() { return new ServerFacade(); }
}
