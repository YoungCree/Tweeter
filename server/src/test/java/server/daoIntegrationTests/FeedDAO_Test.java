package server.daoIntegrationTests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import model.domain.Status;
import model.domain.User;
import model.service.request.FeedRequest;
import server.dao.FeedDAO;

public class FeedDAO_Test {

    private FeedDAO dao;
    private FeedRequest feedRequest;
    private String message = "Integration";

    @BeforeEach
    public void setup() {
        dao = new FeedDAO();

        User user = new User("Integration", "Test", "@integrationTest",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        feedRequest = new FeedRequest(user, 6, null);
    }

    @Test void testGetFeed() {
        Assertions.assertNotNull(dao.getFeed(feedRequest));
    }

    @Test void testBatchFeed() {
        dao.batchPutFeed(Arrays.asList("@Guy5", "@Guy10"),
                message,
                "000",
                "@integrationTest");

        User guy = new User();
        guy.setAlias("@Guy5");
        Assertions.assertEquals(
                dao.getFeed(new FeedRequest(guy, 6, null))
                        .getFeed().getStatusAt(0)
                        .getMessage(), message);
    }
}
