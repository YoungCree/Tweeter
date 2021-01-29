package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import model.domain.AuthToken;
import model.domain.User;
import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;
import server.dao.AuthTokenDAO;
import server.dao.UserDAO;
import server.service.LogoutServiceImpl;

public class LogoutServiceImplTest {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;
    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;
    private LogoutServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User user = new User("First", "Last", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken authToken = new AuthToken();

        validRequest = new LogoutRequest(user, authToken);
        invalidRequest = new LogoutRequest(null, null);

        successResponse = new LogoutResponse();
        AuthTokenDAO mockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mockAuthTokenDAO.logout(validRequest)).thenReturn(successResponse);

        failureResponse = new LogoutResponse("An Exception occurred");
        Mockito.when(mockAuthTokenDAO.logout(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new LogoutServiceImpl());
        Mockito.when(serviceSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDAO);
    }

    @Test
    public void testLogout_validRequest() {
        LogoutResponse response = serviceSpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogout_invalidRequest() {
        LogoutResponse response = serviceSpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
