package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import model.domain.Status;
import model.domain.Story;
import model.domain.User;
import edu.byu.cs.tweeter.model.service.StoryServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.StoryRequest;
import model.service.response.StoryResponse;

public class StoryPresenterTest {

    private StoryRequest request;
    private StoryResponse response;
    private StoryServiceProxy mockStoryServiceProxy;
    private StoryPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Status resultStatus1 = new Status("test", LocalDateTime.now(), null, null, currentUser);
        Status resultStatus2 = new Status("test2", LocalDateTime.now().minusDays(1), null, null, currentUser);
        Status resultStatus3 = new Status("test3", LocalDateTime.now().minusHours(3), null, null, currentUser);

        request = new StoryRequest(currentUser, 3, null);
        response = new StoryResponse(new Story(Arrays.asList(resultStatus1, resultStatus2, resultStatus3)), false);

        mockStoryServiceProxy = Mockito.mock(StoryServiceProxy.class);
        Mockito.when(mockStoryServiceProxy.getStory(request)).thenReturn(response);

        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStoryService()).thenReturn(mockStoryServiceProxy);
    }

    @Test
    public void testGetStory_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryServiceProxy.getStory(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.getStory(request));
    }

    @Test void testGetStory_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockStoryServiceProxy.getStory(request)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStory(request);
        });
    }
}
