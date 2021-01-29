package server.service;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.User;
import model.net.TweeterRemoteException;
import model.service.SignupService;
import model.service.request.SignupRequest;
import server.dao.AuthTokenDAO;
import server.dao.S3DAO;
import server.dao.UserDAO;
import model.service.response.SignupResponse;

public class SignupServiceImpl implements SignupService {

    @Override
    public SignupResponse signup(SignupRequest request) throws IOException {
        if (request.getUsername() != null) {
            if (request.getProfilePic() != null) request.profilePicFromBytes();
            String imageURL = getS3DAO().saveImage(request.getProfilePic(), request.getUsername());
            User user = getUserDAO().createUser(request, imageURL);
            AuthToken authToken = getAuthTokenDAO().getAuthToken(request);
            return createResponse(user, authToken);
        }
        return createResponse(null, null);
    }

    public SignupResponse createResponse(User user, AuthToken authToken) {return new SignupResponse(user, authToken); }

    public UserDAO getUserDAO() { return new UserDAO(); }
    public S3DAO getS3DAO() { return new S3DAO(); }
    public AuthTokenDAO getAuthTokenDAO() { return new AuthTokenDAO(); }
}
