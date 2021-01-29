package model.service.request;

import java.util.Base64;

public class SignupRequest {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private byte [] profilePic;
    private String profilePicString;

    public SignupRequest() {}

    public SignupRequest(String firstName, String lastName, String username, String password, byte [] profilePic) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.profilePic = profilePic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public String getProfilePicString() {
        return profilePicString;
    }

    public void setProfilePicString(String profilePicString) {
        this.profilePicString = profilePicString;
    }

    public void profilePicToString() {
        profilePicString = Base64.getEncoder().encodeToString(profilePic);
    }

    public void profilePicFromBytes() {
        profilePic = Base64.getDecoder().decode(profilePicString);
    }
}
