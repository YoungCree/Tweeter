package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.User;
import edu.byu.cs.tweeter.model.service.LoginServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.LoginRequest;
import model.service.response.LoginResponse;

public class LoginPresenterTest {

    private LoginRequest request;
    private LoginResponse response;
    private LoginServiceProxy mockLoginServiceProxy;
    private LoginPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        request = new LoginRequest("username", "password");
        response = new LoginResponse(user, authToken);

        mockLoginServiceProxy = Mockito.mock(LoginServiceProxy.class);
        Mockito.when(mockLoginServiceProxy.login(request)).thenReturn(response);

        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {}));
        Mockito.when(presenter.getLoginService()).thenReturn(mockLoginServiceProxy);
    }

    @Test
    public void testLogin_returnServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockLoginServiceProxy.login(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.login(request));
    }

    @Test
    public void testLogin_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockLoginServiceProxy.login(request)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.login(request);
        });
    }
}
