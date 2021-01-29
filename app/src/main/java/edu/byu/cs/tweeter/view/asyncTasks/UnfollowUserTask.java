package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.UnfollowUserRequest;
import model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;

public class UnfollowUserTask extends AsyncTask<UnfollowUserRequest, Void, UnfollowUserResponse> {

    private final UserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected UnfollowUserResponse doInBackground(UnfollowUserRequest... unfollowUserRequests) {
        UnfollowUserResponse unfollowUserResponse = null;

        try {
            unfollowUserResponse = presenter.unfollowUser(unfollowUserRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return unfollowUserResponse;
    }

    public interface Observer {
        void unfollowUserSuccessful(UnfollowUserResponse unfollowUserResponse);
        void unfollowUserUnsuccessful(UnfollowUserResponse unfollowUserResponse);
        void handleException(Exception ex);
    }

    public UnfollowUserTask(UserPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(UnfollowUserResponse unfollowUserResponse) {
        if (exception != null) observer.handleException(exception);
        else if (unfollowUserResponse.isSuccess()) observer.unfollowUserSuccessful(unfollowUserResponse);
        else observer.unfollowUserUnsuccessful(unfollowUserResponse);
    }
}