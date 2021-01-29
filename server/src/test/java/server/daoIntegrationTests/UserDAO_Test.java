package server.daoIntegrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.User;
import model.service.request.NumFollowersFollowingRequest;
import model.service.request.SignupRequest;
import server.dao.UserDAO;

public class UserDAO_Test {

    private UserDAO dao;
    private SignupRequest signupRequest;
    private NumFollowersFollowingRequest numFolFolRequest;

    @BeforeEach
    public void setup() {
        dao = new UserDAO();
    }

    @Test
    public void testSignup() {
        signupRequest = new SignupRequest("Integration", "Test", "@integrationTest",
                "easy123", null);
        String profileURL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

        Assertions.assertEquals("@integrationTest", dao.createUser(signupRequest, profileURL).getAlias());
        Assertions.assertEquals("Integration", dao.getUser("@integrationTest").getFirstName());
    }

    @Test
    public void testGetUser() {
        String alias = "@integrationTest";

        Assertions.assertEquals(alias, dao.getUser(alias).getAlias());
        Assertions.assertEquals(alias, dao.getUserByAlias(alias).getAlias());
    }

    @Test
    public void testGetNumFol() {
        String rootAlias = "@creedev";
        User user = new User();
        user.setAlias(rootAlias);

        Assertions.assertTrue(dao.getNumFollow(new NumFollowersFollowingRequest(user)).getNumFollowers() > 0);
    }

    @Test
    public void testUpdateNumFollowing() {
        String rootAlias = "@DeeDempsey";
        String givenAlias = "@integrationTest";

        User rootUser = new User();
        rootUser.setAlias(rootAlias);

        User givenUser = new User();
        givenUser.setAlias(givenAlias);

        numFolFolRequest = new NumFollowersFollowingRequest(rootUser);

        int prevFolCount = dao.getNumFollow(numFolFolRequest).getNumFollowing();
        dao.updateNumFollow(rootAlias, givenAlias, true);
        Assertions.assertEquals(prevFolCount + 1, dao.getNumFollow(numFolFolRequest).getNumFollowing());

        prevFolCount = dao.getNumFollow(numFolFolRequest).getNumFollowing();
        dao.updateNumFollow(rootAlias, givenAlias, false);
        Assertions.assertEquals(prevFolCount - 1, dao.getNumFollow(numFolFolRequest).getNumFollowing());
    }
}
