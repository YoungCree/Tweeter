package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.SignupServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;

public class SignupPresenter {

    private final View view;

    public interface View {}

    public SignupPresenter(View view) {this.view = view;}

    public SignupResponse signup(SignupRequest signupRequest) throws IOException, TweeterRemoteException {
        SignupServiceProxy signupServiceProxy = getSignupService();
        return signupServiceProxy.signup(signupRequest);
    }

    public SignupServiceProxy getSignupService() { return new SignupServiceProxy(); }
}
