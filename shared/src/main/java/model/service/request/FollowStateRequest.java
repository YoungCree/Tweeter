package model.service.request;

import model.domain.User;

public class FollowStateRequest {

    private User rootUser;
    private User userGiven;

    private FollowStateRequest() {}

    public FollowStateRequest(User rootUser, User userGiven) {
        this.rootUser = rootUser;
        this.userGiven = userGiven;
    }

    public User getRootUser() {
        return rootUser;
    }

    public void setRootUser(User rootUser) {
        this.rootUser = rootUser;
    }

    public User getUserGiven() {
        return userGiven;
    }

    public void setUserGiven(User userGiven) {
        this.userGiven = userGiven;
    }
}
