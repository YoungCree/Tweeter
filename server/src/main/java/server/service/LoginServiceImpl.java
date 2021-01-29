package server.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import model.domain.AuthToken;
import model.domain.User;
import model.service.LoginService;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;
import server.dao.AuthTokenDAO;
import server.dao.UserDAO;
import server.helper.Hasher;

public class LoginServiceImpl implements LoginService {

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = getUserDAO().getUser(request.getUsername());
        Hasher hasher = getHasher();
        if (request.getPassword() != null) {
            try {
                if (!hasher.validatePassword(request.getPassword(), user.getPassword()))
                    return new LoginResponse("Incorrect Password");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                return new LoginResponse("Incorrect Password: " + e.toString());
            }
        }
        AuthToken authToken = getAuthTokenDAO().getAuthToken(request);
        return createResponse(user, authToken);
    }

    public LoginResponse createResponse(User user, AuthToken authToken) { return new LoginResponse(user, authToken); }

    public UserDAO getUserDAO() { return new UserDAO(); }
    public AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
    public Hasher getHasher() { return new Hasher(); }
}
