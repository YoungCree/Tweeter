package server.serviceImplTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import model.domain.User;
import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;
import server.dao.FollowingDAO;
import server.service.FollowStateServiceImpl;

public class FollowStateServiceImplTest {

    private FollowStateRequest validRequest;
    private FollowStateRequest invalidRequest;
    private FollowStateResponse successResponse;
    private FollowStateResponse failureResponse;
    private FollowStateServiceImpl serviceSpy;

    @BeforeEach
    public void setup() {
        User rootUser = new User("FirstName", "LastName", null);
        User userGiven = new User("FirstName1", "LastName1", null);

        validRequest = new FollowStateRequest(rootUser, userGiven);
        invalidRequest = new FollowStateRequest(null, null);

        successResponse = new FollowStateResponse(false);
        FollowingDAO mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.followState(validRequest)).thenReturn(successResponse);

        failureResponse = new FollowStateResponse("An Exception occured");
        Mockito.when(mockFollowingDAO.followState(invalidRequest)).thenReturn(failureResponse);

        serviceSpy = Mockito.spy(new FollowStateServiceImpl());
        Mockito.when(serviceSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
    }

    @Test
    public void testGetFollowState_validRequest() {
        FollowStateResponse response = serviceSpy.followState(validRequest);
        Assertions.assertEquals(successResponse, response);
        Assertions.assertFalse(response.getIsFollowing());
    }

    @Test
    public void testGetFollowState_invalidRequest() {
        FollowStateResponse response = serviceSpy.followState(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
