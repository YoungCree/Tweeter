package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;

import model.domain.Status;
import model.domain.User;
import edu.byu.cs.tweeter.model.service.NewTweetServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;

public class NewTweetPresenterTest {

    private NewTweetRequest request;
    private NewTweetResponse response;
    private NewTweetServiceProxy mockNewTweetServiceProxy;
    private NewTweetPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        Status status = new Status("test", LocalDateTime.now(), null, null, user);

        request = new NewTweetRequest(status);
        response = new NewTweetResponse();

        mockNewTweetServiceProxy = Mockito.mock(NewTweetServiceProxy.class);
        Mockito.when(mockNewTweetServiceProxy.postTweet(request)).thenReturn(response);

        presenter = Mockito.spy(new NewTweetPresenter());
        Mockito.when(presenter.getNewTweetService()).thenReturn(mockNewTweetServiceProxy);
    }

    @Test
    public void testNewTweet_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockNewTweetServiceProxy.postTweet(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.postTweet(request));
    }

    @Test
    public void testNewTweet_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockNewTweetServiceProxy.postTweet(request)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.postTweet(request);
        });
    }
}
