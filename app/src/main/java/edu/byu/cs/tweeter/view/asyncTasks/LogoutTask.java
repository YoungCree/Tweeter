package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import model.service.request.LogoutRequest;
import model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;

public class LogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> {

    private final MainPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void logoutSuccessful(LogoutResponse logoutResponse);
        void logoutUnsuccessful(LogoutResponse logoutResponse);
        void handleException(Exception ex);
    }

    public LogoutTask(MainPresenter presenter, Observer observer) {
        if (observer == null) throw new NullPointerException();

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected LogoutResponse doInBackground(LogoutRequest... logoutRequests) {
        LogoutResponse logoutResponse = null;

        try {
            logoutResponse = presenter.logout(logoutRequests[0]);
        } catch (Exception ex) {
            exception = ex;
        }

        return logoutResponse;
    }

    @Override
    protected void onPostExecute(LogoutResponse logoutResponse) {
        if (exception != null) observer.handleException(exception);
        else if (logoutResponse.isSuccess()) observer.logoutSuccessful(logoutResponse);
        else observer.logoutUnsuccessful(logoutResponse);
    }
}
