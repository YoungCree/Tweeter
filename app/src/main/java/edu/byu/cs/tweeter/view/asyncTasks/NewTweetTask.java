package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;
import edu.byu.cs.tweeter.presenter.NewTweetPresenter;

public class NewTweetTask extends AsyncTask<NewTweetRequest, Void, NewTweetResponse> {

    private final NewTweetPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void postTweetSuccessful(NewTweetResponse newTweetResponse);
        void postTweetUnsuccessful(NewTweetResponse newTweetResponse);
        void handleException(Exception ex);
    }

    public NewTweetTask(NewTweetPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected NewTweetResponse doInBackground(NewTweetRequest... newTweetRequests) {
        NewTweetResponse newTweetResponse = null;

        try {
            newTweetResponse = presenter.postTweet(newTweetRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return newTweetResponse;
    }

    @Override
    protected void onPostExecute(NewTweetResponse newTweetResponse) {
        if (exception != null) observer.handleException(exception);
        else if (newTweetResponse.isSuccess()) observer.postTweetSuccessful(newTweetResponse);
        else observer.postTweetUnsuccessful(newTweetResponse);
    }
}
