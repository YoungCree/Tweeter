package model.service.request;

import model.domain.User;

public class NumFollowersFollowingRequest {

    private User user;

    private NumFollowersFollowingRequest() {}

    public NumFollowersFollowingRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
