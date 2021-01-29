package model.service.request;

import model.domain.User;

public class UnfollowUserRequest {
    private User rootUser;
    private User userToUnfollow;

    private UnfollowUserRequest() {}

    public UnfollowUserRequest(User rootUser, User userToUnfollow) {
        this.rootUser = rootUser;
        this.userToUnfollow = userToUnfollow;
    }

    public User getRootUser() {
        return rootUser;
    }

    public void setRootUser(User rootUser) {
        this.rootUser = rootUser;
    }

    public User getUserToUnfollow() {
        return userToUnfollow;
    }

    public void setUserToUnfollow(User userToUnfollow) {
        this.userToUnfollow = userToUnfollow;
    }
}
