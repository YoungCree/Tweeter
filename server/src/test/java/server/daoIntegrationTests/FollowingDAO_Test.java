package server.daoIntegrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import model.service.request.FollowStateRequest;
import model.service.request.FollowUserRequest;
import model.service.request.FollowerRequest;
import model.service.request.FollowingRequest;
import model.service.request.UnfollowUserRequest;
import server.dao.FollowingDAO;

public class FollowingDAO_Test {

    private FollowingDAO dao;
    private FollowStateRequest followStateRequest;
    private FollowUserRequest followUserRequest;
    private UnfollowUserRequest unfollowUserRequest;
    private FollowerRequest followerRequest;
    private FollowingRequest followingRequest;
    private String rootUserAlias = "@Guy1";
    private String givenUserAlias = "@Guy15";

    @BeforeEach
    public void setup() {
        dao = new FollowingDAO();
    }

    @Test
    public void testFollowUnfollowStateUser() {
        User rootUser = new User();
        rootUser.setAlias(rootUserAlias);

        User givenUser = new User();
        givenUser.setAlias(givenUserAlias);

        followUserRequest = new FollowUserRequest(givenUser, rootUser);
        unfollowUserRequest = new UnfollowUserRequest(givenUser, rootUser);
        followStateRequest = new FollowStateRequest(givenUser, rootUser);

        Assertions.assertTrue(dao.followUser(followUserRequest).isSuccess());
        Assertions.assertTrue(dao.followState(followStateRequest).getIsFollowing());
        Assertions.assertTrue(dao.unfollowUser(unfollowUserRequest).isSuccess());
        Assertions.assertFalse(dao.followState(followStateRequest).getIsFollowing());
    }

    @Test
    public void testGetFollowers() {
        User rootUser = new User();
        rootUser.setAlias(rootUserAlias);

        followerRequest = new FollowerRequest(rootUser, 6, null);

        Assertions.assertTrue(dao.getFollowers(followerRequest).getFollowers().size() > 0);
    }

    @Test
    public void testGetFollowing() {
        User rootUser = new User();
        rootUser.setAlias(rootUserAlias);

        followingRequest = new FollowingRequest(rootUser, 6, null);

        Assertions.assertTrue(dao.getFollowees(followingRequest).getFollowees().size() > 0);
    }
}
