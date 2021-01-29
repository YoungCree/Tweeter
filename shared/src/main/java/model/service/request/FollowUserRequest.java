package model.service.request;

import model.domain.User;

public class FollowUserRequest {

    private User rootUser;
    private User userToFollow;

    private FollowUserRequest() {}

    public FollowUserRequest(User rootUser, User userToFollow) {
        this.rootUser = rootUser;
        this.userToFollow = userToFollow;
    }

    public User getRootUser() {
        return rootUser;
    }

    public void setRootUser(User rootUser) {
        this.rootUser = rootUser;
    }

    public User getUserToFollow() {
        return userToFollow;
    }

    public void setUserToFollow(User userToFollow) {
        this.userToFollow = userToFollow;
    }
}
