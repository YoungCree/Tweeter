package model.service.request;

import model.domain.Status;
import model.domain.User;

public class StoryRequest {

    private User user;
    private int limit;
    private Status lastStatus;

    private StoryRequest() {}

    public StoryRequest(User user, int limit, Status lastStatus) {
        this.user = user;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public User getUser() { return  user; }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLimit() { return  limit; }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Status getLastStatus() { return lastStatus; }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}
