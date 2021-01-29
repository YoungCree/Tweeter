package model.service.request;

import model.domain.Status;

public class NewTweetRequest {

    private Status status;

    private NewTweetRequest() {}

    public NewTweetRequest(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
