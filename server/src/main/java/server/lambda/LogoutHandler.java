package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import server.service.LogoutServiceImpl;
import model.service.response.LogoutResponse;
import model.service.request.LogoutRequest;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest logoutRequest, Context context) {
        if (logoutRequest.getAuthToken() == null || logoutRequest.getUser() == null)
            throw new RuntimeException("[BadRequest] authToken or user null");

        LogoutServiceImpl service = new LogoutServiceImpl();
        return service.logout(logoutRequest);
    }
}
