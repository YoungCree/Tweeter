package model.service.request;

public class UserByAliasRequest {

    private String userAlias;

    private UserByAliasRequest() {}

    public UserByAliasRequest(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }
}
