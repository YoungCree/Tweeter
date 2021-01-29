package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.service.request.LoginRequest;
import model.service.response.LoginResponse;
import server.service.LoginServiceImpl;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(LoginRequest loginRequest, Context context) {
        if (loginRequest.getPassword() == null || loginRequest.getUsername() == null)
            throw new RuntimeException("[BadRequest] password or username null");

        LoginServiceImpl loginService = new LoginServiceImpl();
        return loginService.login(loginRequest);
    }
}
