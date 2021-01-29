package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LogoutServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;

public class MainPresenter extends UserMainPresenter {

    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        LogoutServiceProxy logoutServiceProxy = getLogoutService();
        return logoutServiceProxy.logout(request);
    }

    public LogoutServiceProxy getLogoutService() { return new LogoutServiceProxy(); }
}
