package model.service;

import java.io.IOException;

import model.net.TweeterRemoteException;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;

public interface SignupService {

    public SignupResponse signup(SignupRequest request) throws IOException, TweeterRemoteException;
}
