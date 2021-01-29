package server.daoIntegrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.domain.AuthToken;
import model.service.request.LoginRequest;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;
import server.dao.AuthTokenDAO;

public class AuthTokenDAO_Test {

    private AuthTokenDAO dao;
    private String alias = "@integrationTest";
    private LoginRequest loginRequest;
    private SignupRequest signupRequest;

    @BeforeEach
    public void setup() {
        dao = new AuthTokenDAO();

        signupRequest = new SignupRequest();
        signupRequest.setFirstName("Integration");
        signupRequest.setLastName("Test");
        signupRequest.setProfilePicString("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        signupRequest.setUsername(alias);
        signupRequest.setPassword("easy123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername(alias);
        loginRequest.setPassword("easy123");
    }

    @Test void testSignup() {
        AuthToken token = dao.getAuthToken(signupRequest);
        Assertions.assertNotNull(token);
    }

    @Test void testLogin() {
        AuthToken token = dao.getAuthToken(loginRequest);
        Assertions.assertNotNull(token);
    }

    @Test void testVerify() {
        Assertions.assertTrue(dao.validAuth(alias));
    }
}
