package model.service.request;

import model.domain.User;

public class FollowerRequest {

    private User followee;
    private int limit;
    private User lastFollower;

    private FollowerRequest() {}

    public FollowerRequest(User followee, int limit, User lastFollower) {
        this.followee = followee;
        this.limit = limit;
        this.lastFollower = lastFollower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public User getLastFollower() {
        return lastFollower;
    }

    public void setLastFollower(User lastFollower) {
        this.lastFollower = lastFollower;
    }
}
