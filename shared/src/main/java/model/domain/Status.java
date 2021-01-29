package model.domain;

//import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Status {

    private String message;
    private LocalDateTime timestamp;
    private String timestampString;
    private List<User> userMentions;
    private List<String> urls;
    private User user;

    public Status() {}

    public Status(String message, LocalDateTime timestamp, List<User> userMentions, List<String> urls, User user) {

        this.message = message;
        this.timestamp = timestamp;
        this.userMentions = userMentions;
        this.urls = urls;
        this.user = user;
    }

    public Status(String message, String timestampString, List<User> userMentions, List<String> urls, User user) {

        this.message = message;
        this.timestampString = timestampString;
        this.userMentions = userMentions;
        this.urls = urls;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }

    public String getTimestampString() {
        return timestampString;
    }

    public void setTimestampString(String timestampString) {
        this.timestampString = timestampString;
    }

    public void setDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        timestamp = LocalDateTime.parse(timestampString, formatter);
    }

    public List<User> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<User> userMentions) {
        this.userMentions = userMentions;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object obj) {
        //if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Status status = (Status) obj;
        return message.equals(status.message) && user.equals(status.user);
    }
}
