package server.daoIntegrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.Status;
import model.domain.User;
import model.service.request.NewTweetRequest;
import model.service.request.StoryRequest;
import server.dao.StoryDAO;

public class StoryDAO_Test {

    private StoryDAO dao;
    private StoryRequest storyRequest;
    private User user;
    private String message = "Integration";

    @BeforeEach
    public void setup() {
        dao = new StoryDAO();

        user = new User("Integration", "Test", "@integrationTest",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        storyRequest = new StoryRequest(user, 6, null);
    }

    @Test
    void testPostStory() {
        Status status = new Status();
        status.setTimestampString("2020-12-07 12:34");
        status.setUser(user);
        status.setMessage("Integration Story");
        NewTweetRequest request = new NewTweetRequest(status);

        Assertions.assertTrue(dao.postTweet(request).isSuccess());
        Assertions.assertEquals(dao.getStory(storyRequest)
                        .getStory().getStatusAt(0).getMessage(),
                "Integration Story");
    }

    @Test
    void testGetStory() {
        Assertions.assertNotNull(dao.getStory(storyRequest));
    }
}
