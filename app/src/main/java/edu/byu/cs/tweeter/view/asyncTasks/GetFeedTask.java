package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.FeedRequest;
import model.service.response.FeedResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;

public class GetFeedTask extends AsyncTask<FeedRequest, Void, FeedResponse> {

    private final FeedPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected FeedResponse doInBackground(FeedRequest... feedRequests) {
        FeedResponse response = null;

        try {
            response = presenter.getFeed(feedRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(FeedResponse feedResponse) {
        if (exception != null) observer.handleException(exception);
        else observer.feedRetrieved(feedResponse);
    }

    public interface Observer {
        void feedRetrieved(FeedResponse feedResponse);
        void handleException(Exception exception);
    }

    public GetFeedTask(FeedPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }
}
