package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import model.service.UserByAliasService;
import server.service.UserByAliasServiceImpl;
import model.service.response.UserByAliasResponse;
import model.service.request.UserByAliasRequest;

public class GetUserByAliasHandler implements RequestHandler<UserByAliasRequest, UserByAliasResponse> {
    @Override
    public UserByAliasResponse handleRequest(UserByAliasRequest request, Context context) {
        if (request.getUserAlias() == null)
            throw new RuntimeException("[BadRequest] userAlias null");

        UserByAliasServiceImpl service = new UserByAliasServiceImpl();
        return service.getUserByAlias(request);
    }
}
