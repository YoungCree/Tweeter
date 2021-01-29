package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.NumFollowersFollowingRequest;
import model.service.response.NumFollowersFollowingResponse;
import edu.byu.cs.tweeter.presenter.UserMainPresenter;

public class GetNumFollowersFollowingTask extends AsyncTask<NumFollowersFollowingRequest, Void, NumFollowersFollowingResponse> {

    private final UserMainPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void getNumFollowersFollowingSuccessful(NumFollowersFollowingResponse response);
        void getNumFollowersFollowingUnsuccessful(NumFollowersFollowingResponse response);
        void handleException(Exception ex);
    }

    public GetNumFollowersFollowingTask(UserMainPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected NumFollowersFollowingResponse doInBackground(NumFollowersFollowingRequest... numFollowersFollowingRequests) {
        NumFollowersFollowingResponse response = null;

        try {
            response = presenter.getNumFollowersFollowing(numFollowersFollowingRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(NumFollowersFollowingResponse response) {
        if (exception != null) observer.handleException(exception);
        else if (response.isSuccess()) observer.getNumFollowersFollowingSuccessful(response);
        else observer.getNumFollowersFollowingUnsuccessful(response);
    }
}
