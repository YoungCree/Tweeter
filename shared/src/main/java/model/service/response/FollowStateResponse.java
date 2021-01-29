package model.service.response;

public class FollowStateResponse extends Response {

    private boolean isFollowing;

    public FollowStateResponse(String message) { super(false, message); }

    public FollowStateResponse(boolean isFollowing) {
        super(true, null);
        this.isFollowing = isFollowing;
    }

    public boolean getIsFollowing() {
        return isFollowing;
    }
}
