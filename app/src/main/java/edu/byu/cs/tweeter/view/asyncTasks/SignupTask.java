package edu.byu.cs.tweeter.view.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import model.domain.User;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;
import edu.byu.cs.tweeter.presenter.SignupPresenter;
import edu.byu.cs.tweeter.util.ByteArrayUtils;

public class SignupTask extends AsyncTask<SignupRequest, Void, SignupResponse> {

    private final SignupPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void signupSuccessful(SignupResponse signupResponse);
        void signupUnsuccessful(SignupResponse signupResponse);
        void handleException(Exception ex);
    }

    public SignupTask(SignupPresenter presenter, Observer observer) {
        if (observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected SignupResponse doInBackground(SignupRequest... signupRequests) {
        SignupResponse signupResponse = null;

        try {
            signupResponse = presenter.signup(signupRequests[0]);

            if (signupResponse.isSuccess()) {
                loadImage(signupResponse.getUser());
            }
        } catch (Exception ex) {
            exception = ex;
        }

        return signupResponse;
    }

    private void loadImage(User user) {
        try {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        } catch (IOException e) {
            Log.e(this.getClass().getName(), e.toString(), e);
        }
    }

    @Override
    protected void onPostExecute(SignupResponse signupResponse) {
        if (exception != null) {
            observer.handleException(exception);
        } else if (signupResponse.isSuccess()) {
            observer.signupSuccessful(signupResponse);
        } else {
            observer.signupUnsuccessful(signupResponse);
        }
    }
}
