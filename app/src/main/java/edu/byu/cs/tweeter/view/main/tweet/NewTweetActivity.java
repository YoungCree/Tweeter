package edu.byu.cs.tweeter.view.main.tweet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDateTime;

import edu.byu.cs.tweeter.R;
import model.domain.AuthToken;
import model.domain.Status;
import model.domain.User;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;
import edu.byu.cs.tweeter.presenter.NewTweetPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.NewTweetTask;

public class NewTweetActivity extends AppCompatActivity implements NewTweetTask.Observer {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final String LOG_TAG = "NewTweetActivity";

    private EditText tweetBox;
    private Button tweetButton;

    private User m_user;

    private NewTweetPresenter presenter;

    @Override
    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);

        m_user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        presenter = new NewTweetPresenter();
        NewTweetTask.Observer observer = this;

        getWindow().setEnterTransition(new Slide());
        getWindow().setExitTransition(new Slide());

        tweetButton = findViewById(R.id.tweetButton);

        // Give the edit field focus
        tweetBox = findViewById(R.id.tweetInput);
        tweetBox.requestFocus();

        // Force the keyboard to show
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tweetButton.setEnabled(false);

        tweetBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tweetButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) tweetButton.setEnabled(false);
            }
        });

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Status tweet = new Status(tweetBox.getText().toString(), LocalDateTime.now(), null, null, m_user);
                NewTweetRequest newTweetRequest = new NewTweetRequest(tweet);
                NewTweetTask newTweetTask = new NewTweetTask(presenter, observer);
                newTweetTask.execute(newTweetRequest);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = findViewById(R.id.tweetInput).getRootView();
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        finish();
        return true;
    }

    @Override
    public void postTweetSuccessful(NewTweetResponse newTweetResponse) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = findViewById(R.id.tweetInput).getRootView();
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        finish();
        Toast.makeText(getApplicationContext(), "Tweet successfully posted.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void postTweetUnsuccessful(NewTweetResponse newTweetResponse) {
        Toast.makeText(getApplicationContext(), "Tweet couldn't be posted. " + newTweetResponse.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
