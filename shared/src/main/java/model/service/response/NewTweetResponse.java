package model.service.response;

public class NewTweetResponse extends Response {

    public NewTweetResponse(String message) { super(false, message); }

    public NewTweetResponse() { super(true, null); }
}
