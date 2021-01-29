package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;

public interface LogoutService {

    LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException;
}
