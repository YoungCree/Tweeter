package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.FollowerRequest;
import model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.presenter.FollowerPresenter;

public class GetFollowersTask extends AsyncTask<FollowerRequest, Void, FollowerResponse> {

    private final FollowerPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected FollowerResponse doInBackground(FollowerRequest... followerRequests) {

        FollowerResponse response = null;

        try {
            response = presenter.getFollower(followerRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(FollowerResponse followerResponse) {
        if (exception != null) observer.handleException(exception);
        else observer.followersRetrieved(followerResponse);
    }

    public interface Observer {
        void followersRetrieved(FollowerResponse followerResponse);
        void handleException(Exception exception);
    }

    public GetFollowersTask(FollowerPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }
}
