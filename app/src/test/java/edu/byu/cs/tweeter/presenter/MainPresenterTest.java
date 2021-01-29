package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.User;
import edu.byu.cs.tweeter.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.service.NumFollowersFollowingServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.LogoutRequest;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.LogoutResponse;
import model.service.response.NumFollowersFollowingResponse;

public class MainPresenterTest {

    private LogoutRequest logoutRequest;
    private LogoutResponse logoutResponse;
    private LogoutServiceProxy mockLogoutServiceProxy;

    private NumFollowersFollowingRequest numFolFolRequest;
    private NumFollowersFollowingResponse numFolFolResponse;
    private NumFollowersFollowingServiceProxy mockNumFolFolService;

    private MainPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("first", "last", null);
        AuthToken authToken = new AuthToken();

        logoutRequest = new LogoutRequest(user, authToken);
        logoutResponse = new LogoutResponse();

        mockLogoutServiceProxy = Mockito.mock(LogoutServiceProxy.class);
        Mockito.when(mockLogoutServiceProxy.logout(logoutRequest)).thenReturn(logoutResponse);

        numFolFolRequest = new NumFollowersFollowingRequest(user);
        numFolFolResponse = new NumFollowersFollowingResponse(4, 4);

        mockNumFolFolService = Mockito.mock(NumFollowersFollowingServiceProxy.class);
        Mockito.when(mockNumFolFolService.getNumFollowersFollowing(numFolFolRequest)).thenReturn(numFolFolResponse);

        presenter = Mockito.spy(new MainPresenter());
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutServiceProxy);
        Mockito.when(presenter.getNumFollowersFollowingService()).thenReturn(mockNumFolFolService);
    }

    @Test
    public void testLogout_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutServiceProxy.logout(logoutRequest)).thenReturn(logoutResponse);
        Assertions.assertEquals(logoutResponse, presenter.logout(logoutRequest));
    }

    @Test void testLogout_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockLogoutServiceProxy.logout(logoutRequest)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.logout(logoutRequest);
        });
    }

    @Test void testGetNumFolFol_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockNumFolFolService.getNumFollowersFollowing(numFolFolRequest)).thenReturn(numFolFolResponse);
        Assertions.assertEquals(numFolFolResponse, presenter.getNumFollowersFollowing(numFolFolRequest));
    }

    @Test void testGetNumFolFol_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockNumFolFolService.getNumFollowersFollowing(numFolFolRequest)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.getNumFollowersFollowing(numFolFolRequest);
        });
    }
}
