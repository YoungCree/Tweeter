package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import model.domain.Feed;
import model.domain.Status;
import model.domain.User;
import edu.byu.cs.tweeter.model.service.FeedServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.FeedRequest;
import model.service.response.FeedResponse;

public class FeedPresenterTest {

    private FeedRequest request;
    private FeedResponse response;
    private FeedServiceProxy mockFeedServiceProxy;
    private FeedPresenter presenter;
    private String URL = "/getfeed";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status resultStatus1 = new Status("test", LocalDateTime.now(), null, null, currentUser);
        Status resultStatus2 = new Status("test2", LocalDateTime.now().minusDays(1), null, null, currentUser);
        Status resultStatus3 = new Status("test3", LocalDateTime.now().minusHours(3), null, null, currentUser);

        request = new FeedRequest(currentUser, 3, null);
        response = new FeedResponse(new Feed(Arrays.asList(resultStatus1, resultStatus2, resultStatus3)), false);

        mockFeedServiceProxy = Mockito.mock(FeedServiceProxy.class);
        Mockito.when(mockFeedServiceProxy.getFeed(request)).thenReturn(response);

        presenter = Mockito.spy(new FeedPresenter(new FeedPresenter.View() {}));
        Mockito.when(presenter.getFeedService()).thenReturn(mockFeedServiceProxy);
    }

    @Test
    public void testGetFeed_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFeedServiceProxy.getFeed(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.getFeed(request));
    }

    @Test void testGetFeed_serviceThrowsIOExcpetion_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFeedServiceProxy.getFeed(request)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFeed(request);
        });
    }
}
