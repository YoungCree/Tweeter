package edu.byu.cs.tweeter.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.LoginServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LoginPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a login request.
     *
     * @param loginRequest the request.
     */
    public LoginResponse login(LoginRequest loginRequest) throws IOException, TweeterRemoteException {
        LoginServiceProxy loginServiceProxy = getLoginService();
        return loginServiceProxy.login(loginRequest);
    }

    public LoginServiceProxy getLoginService() { return new LoginServiceProxy(); }
}
