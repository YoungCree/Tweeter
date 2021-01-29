package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import model.domain.AuthToken;
import model.domain.User;
import edu.byu.cs.tweeter.model.service.SignupServiceProxy;
import model.net.TweeterRemoteException;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;

public class SignupPresenterTest {

    private SignupRequest request;
    private SignupResponse response;
    private SignupServiceProxy mockSignupServiceProxy;
    private SignupPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User user = new User("first", "last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        AuthToken authToken = new AuthToken();

        request = new SignupRequest("first", "last", "username", "password", null);
        response = new SignupResponse(user, authToken);

        mockSignupServiceProxy = Mockito.mock(SignupServiceProxy.class);
        Mockito.when(mockSignupServiceProxy.signup(request)).thenReturn(response);

        presenter = Mockito.spy(new SignupPresenter(new SignupPresenter.View() {}));
        Mockito.when(presenter.getSignupService()).thenReturn(mockSignupServiceProxy);
    }

    @Test
    public void testSignup_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockSignupServiceProxy.signup(request)).thenReturn(response);
        Assertions.assertEquals(response, presenter.signup(request));
    }

    @Test
    public void testSignup_serviceThrowsException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockSignupServiceProxy.signup(request)).thenThrow(new IOException());
        Assertions.assertThrows(IOException.class, () -> {
            presenter.signup(request);
        });
    }
}
