package model.domain;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {

    private String token;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public AuthToken() { generateNewToken(); }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        this.token =  base64Encoder.encodeToString(randomBytes);
    }
}
