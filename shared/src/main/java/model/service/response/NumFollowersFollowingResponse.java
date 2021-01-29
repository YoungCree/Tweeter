package model.service.response;

public class NumFollowersFollowingResponse extends Response {

    private int numFollowers;
    private int numFollowing;

    public NumFollowersFollowingResponse(String message) { super(false, message); }

    public NumFollowersFollowingResponse(int numFollowers, int numFollowing) {
        super(true, null);
        this.numFollowers = numFollowers;
        this.numFollowing = numFollowing;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public int getNumFollowing() {
        return numFollowing;
    }
}
