package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException;
}
