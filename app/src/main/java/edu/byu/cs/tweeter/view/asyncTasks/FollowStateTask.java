package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.FollowStateRequest;
import model.service.response.FollowStateResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;

public class FollowStateTask extends AsyncTask<FollowStateRequest, Void, FollowStateResponse> {

    private final UserPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected FollowStateResponse doInBackground(FollowStateRequest... followStateRequests) {
        FollowStateResponse followStateResponse = null;

        try {
            followStateResponse = presenter.followState(followStateRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return followStateResponse;
    }

    public interface Observer {
        void followStateSuccessful(FollowStateResponse followStateResponse);
        void followStateUnsuccessful(FollowStateResponse followStateResponse);
        void handleException(Exception ex);
    }

    public FollowStateTask(UserPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(FollowStateResponse followStateResponse) {
        if (exception != null) observer.handleException(exception);
        else if (followStateResponse.isSuccess()) observer.followStateSuccessful(followStateResponse);
        else observer.followStateUnsuccessful(followStateResponse);
    }
}
