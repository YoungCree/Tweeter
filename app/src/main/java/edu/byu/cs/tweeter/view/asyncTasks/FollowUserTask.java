package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.FollowUserRequest;
import model.service.response.FollowUserResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;

public class FollowUserTask extends AsyncTask<FollowUserRequest, Void, FollowUserResponse> {

    private final UserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected FollowUserResponse doInBackground(FollowUserRequest... followUserRequests) {
        FollowUserResponse followUserResponse = null;

        try {
            followUserResponse = presenter.followUser(followUserRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return followUserResponse;
    }

    public interface Observer {
        void followUserSuccessful(FollowUserResponse followUserResponse);
        void followUserUnsuccessful(FollowUserResponse followUserResponse);
        void handleException(Exception ex);
    }

    public FollowUserTask(UserPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(FollowUserResponse followUserResponse) {
        if (exception != null) observer.handleException(exception);
        else if (followUserResponse.isSuccess()) observer.followUserSuccessful(followUserResponse);
        else observer.followUserUnsuccessful(followUserResponse);
    }
}
