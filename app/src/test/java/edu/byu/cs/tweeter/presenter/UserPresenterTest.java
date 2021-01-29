package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.User;
import edu.byu.cs.tweeter.model.service.FollowStateServiceProxy;
import edu.byu.cs.tweeter.model.service.FollowUserServiceProxy;
import edu.byu.cs.tweeter.model.service.NumFollowersFollowingServiceProxy;
import edu.byu.cs.tweeter.model.service.UnfollowUserServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.FollowStateRequest;
import model.service.request.FollowUserRequest;
import model.service.request.NumFollowersFollowingRequest;
import model.service.request.UnfollowUserRequest;
import model.service.response.FollowStateResponse;
import model.service.response.FollowUserResponse;
import model.service.response.NumFollowersFollowingResponse;
import model.service.response.UnfollowUserResponse;

public class UserPresenterTest {

    private FollowUserRequest followUserRequest;
    private FollowUserResponse followUserResponse;
    private FollowUserServiceProxy mockFollowUserServiceProxy;

    private UnfollowUserRequest unfollowUserRequest;
    private UnfollowUserResponse unfollowUserResponse;
    private UnfollowUserServiceProxy mockUnfollowUserServiceProxy;

    private FollowStateRequest followStateRequest;
    private FollowStateResponse followStateResponse;
    private FollowStateServiceProxy mockFollowStateServiceProxy;

    private NumFollowersFollowingRequest numFolFolRequest;
    private NumFollowersFollowingResponse numFolFolResponse;
    private NumFollowersFollowingServiceProxy mockNumFolFolService;

    private UserPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User rootUser = new User("root", "user", null);
        User userGiven = new User("user", "given", null);

        followUserRequest = new FollowUserRequest(rootUser, userGiven);
        followUserResponse = new FollowUserResponse();
        mockFollowUserServiceProxy = Mockito.mock(FollowUserServiceProxy.class);
        Mockito.when(mockFollowUserServiceProxy.followUser(followUserRequest)).thenReturn(followUserResponse);

        unfollowUserRequest = new UnfollowUserRequest(rootUser, userGiven);
        unfollowUserResponse = new UnfollowUserResponse();
        mockUnfollowUserServiceProxy = Mockito.mock(UnfollowUserServiceProxy.class);
        Mockito.when(mockUnfollowUserServiceProxy.unfollowUser(unfollowUserRequest)).thenReturn(unfollowUserResponse);

        followStateRequest = new FollowStateRequest(rootUser, userGiven);
        followStateResponse = new FollowStateResponse(true);
        mockFollowStateServiceProxy = Mockito.mock(FollowStateServiceProxy.class);
        Mockito.when(mockFollowStateServiceProxy.followState(followStateRequest)).thenReturn(followStateResponse);

        numFolFolRequest = new NumFollowersFollowingRequest(userGiven);
        numFolFolResponse = new NumFollowersFollowingResponse(5, 5);
        mockNumFolFolService = Mockito.mock(NumFollowersFollowingServiceProxy.class);
        Mockito.when(mockNumFolFolService.getNumFollowersFollowing(numFolFolRequest)).thenReturn(numFolFolResponse);

        presenter = Mockito.spy(new UserPresenter());
        Mockito.when(presenter.getFollowUserService()).thenReturn(mockFollowUserServiceProxy);
        Mockito.when(presenter.getUnfollowUserService()).thenReturn(mockUnfollowUserServiceProxy);
        Mockito.when(presenter.getFollowStateService()).thenReturn(mockFollowStateServiceProxy);
        Mockito.when(presenter.getNumFollowersFollowingService()).thenReturn(mockNumFolFolService);
    }

    @Test
    public void testFollowUser_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowUserServiceProxy.followUser(followUserRequest)).thenReturn(followUserResponse);
        Assertions.assertEquals(followUserResponse, presenter.followUser(followUserRequest));
    }

    @Test
    public void testFollowUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowUserServiceProxy.followUser(followUserRequest)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
           presenter.followUser(followUserRequest);
        });
    }

    @Test
    public void testUnfollowUser_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockUnfollowUserServiceProxy.unfollowUser(unfollowUserRequest)).thenReturn(unfollowUserResponse);
        Assertions.assertEquals(unfollowUserResponse, presenter.unfollowUser(unfollowUserRequest));
    }

    @Test
    public void testUnfollowUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockUnfollowUserServiceProxy.unfollowUser(unfollowUserRequest)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.unfollowUser(unfollowUserRequest);
        });
    }

    @Test
    public void testFollowState_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowStateServiceProxy.followState(followStateRequest)).thenReturn(followStateResponse);
        Assertions.assertEquals(followStateResponse, presenter.followState(followStateRequest));
    }

    @Test
    public void testFollowState_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockFollowStateServiceProxy.followState(followStateRequest)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
           presenter.followState(followStateRequest);
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
