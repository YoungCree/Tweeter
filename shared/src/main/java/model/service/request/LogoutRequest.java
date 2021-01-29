package model.service.request;

import model.domain.AuthToken;
import model.domain.User;

public class LogoutRequest {

    private User user;
    private AuthToken authToken;

    private LogoutRequest() {}

    public LogoutRequest(User user, AuthToken authToken) {
        this.user = user;
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
