package model.service.response;

import model.domain.User;

public class UserByAliasResponse extends Response {

    private User user;

    public UserByAliasResponse(String message) { super(false, message); }

    public UserByAliasResponse(User user) {
        super(true, null);
        this.user = user;
    }

    public User getUser() { return user; }
}
