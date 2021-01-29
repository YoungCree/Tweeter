package model.service.response;

import model.domain.AuthToken;
import model.domain.User;

public class SignupResponse extends Response {

    private User user;
    private AuthToken authToken;

    public SignupResponse(String message) { super(false, message); }

    public SignupResponse(User user, AuthToken authToken) {
        super(true, null);
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() { return user; }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
