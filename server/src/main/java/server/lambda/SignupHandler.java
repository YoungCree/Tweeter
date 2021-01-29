package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import model.service.request.SignupRequest;
import model.service.response.SignupResponse;
import server.service.SignupServiceImpl;

public class SignupHandler implements RequestHandler<SignupRequest, SignupResponse> {
    @Override
    public SignupResponse handleRequest(SignupRequest request, Context context) {
        if (request.getFirstName() == null || request.getLastName() == null ||
            request.getPassword() == null || request.getUsername() == null)
            throw new RuntimeException("[BadRequest] first/last name, username, or password null");

        SignupServiceImpl service = new SignupServiceImpl();
        try {
            return service.signup(request);
        } catch (IOException e) {
            return new SignupResponse(e.toString());
        }
    }
}
