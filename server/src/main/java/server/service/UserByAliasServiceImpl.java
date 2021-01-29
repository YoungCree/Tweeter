package server.service;

import java.io.IOException;

import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.UserByAliasService;
import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;
import server.dao.UserDAO;

public class UserByAliasServiceImpl implements UserByAliasService {
    @Override
    public UserByAliasResponse getUserByAlias(UserByAliasRequest request) {
        User user = getUserDAO().getUserByAlias(request.getUserAlias());
        return createResponse(user);
    }

    public UserByAliasResponse createResponse(User user) { return new UserByAliasResponse(user); }

    public UserDAO getUserDAO() { return new UserDAO(); }
}
