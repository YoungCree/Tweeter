package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.UserByAliasRequest;
import model.service.response.UserByAliasResponse;
import edu.byu.cs.tweeter.presenter.FeedPresenter;
import edu.byu.cs.tweeter.presenter.FeedStoryPresenter;
import edu.byu.cs.tweeter.presenter.StoryPresenter;

public class GetUserByAliasTask extends AsyncTask<UserByAliasRequest, Void, UserByAliasResponse> {

    private final FeedStoryPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected UserByAliasResponse doInBackground(UserByAliasRequest... userByAliasRequests) {
        UserByAliasResponse userByAliasResponse = null;

        try {
            userByAliasResponse = presenter.getUserByAlias(userByAliasRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return userByAliasResponse;
    }

    public interface Observer {
        void getUserByAliasSuccessful(UserByAliasResponse userByAliasResponse);
        void getUserByAliasUnsuccessful(UserByAliasResponse userByAliasResponse);
        void handleException(Exception ex);
    }

    public GetUserByAliasTask(FeedPresenter presenter, Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    public GetUserByAliasTask(StoryPresenter presenter, Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(UserByAliasResponse userByAliasResponse) {
        if (exception != null) observer.handleException(exception);
        else if (userByAliasResponse.isSuccess()) observer.getUserByAliasSuccessful(userByAliasResponse);
        else observer.getUserByAliasUnsuccessful(userByAliasResponse);
    }
}
