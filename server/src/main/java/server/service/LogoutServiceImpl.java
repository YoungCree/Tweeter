package server.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.LogoutService;
import server.dao.AuthTokenDAO;
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;

public class LogoutServiceImpl implements LogoutService {

    @Override
    public LogoutResponse logout(LogoutRequest request) {
        return getAuthTokenDAO().logout(request);
    }

    public AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
