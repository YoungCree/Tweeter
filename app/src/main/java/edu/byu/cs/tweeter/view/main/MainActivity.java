package edu.byu.cs.tweeter.view.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import edu.byu.cs.tweeter.R;
import model.domain.AuthToken;
import model.domain.User;
import model.service.request.LogoutRequest;
import model.service.request.NumFollowersFollowingRequest;
import model.service.response.LogoutResponse;
import model.service.response.NumFollowersFollowingResponse;
import edu.byu.cs.tweeter.presenter.MainPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetNumFollowersFollowingTask;
import edu.byu.cs.tweeter.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.view.main.tweet.NewTweetActivity;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, GetNumFollowersFollowingTask.Observer, LogoutTask.Observer {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final String LOG_TAG = "MainActivity";

    private MainPresenter presenter;
    private LogoutTask.Observer logoutObserver;

    private User m_user;
    private AuthToken m_authToken;

    private TextView followeeCount;
    private TextView followerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter();
        logoutObserver = this;

        m_user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(m_user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        m_authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), m_user, m_authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(21)
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTweetActivity.class);

                intent.putExtra(NewTweetActivity.CURRENT_USER_KEY, m_user);
                intent.putExtra(NewTweetActivity.AUTH_TOKEN_KEY, m_authToken);

                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            }
        });

        TextView userName = findViewById(R.id.userName);
        userName.setText(m_user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(m_user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(m_user.getImageBytes()));

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.inflate(R.menu.logout_menu);
                popupMenu.show();
            }
        });

        NumFollowersFollowingRequest request = new NumFollowersFollowingRequest(m_user);
        GetNumFollowersFollowingTask getNumFollowersFollowingTask = new GetNumFollowersFollowingTask(presenter, this);
        getNumFollowersFollowingTask.execute(request);

        followeeCount = findViewById(R.id.followeeCount);
        followerCount = findViewById(R.id.followerCount);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                LogoutRequest logoutRequest = new LogoutRequest(m_user, m_authToken);
                LogoutTask logoutTask = new LogoutTask(presenter, logoutObserver);
                logoutTask.execute(logoutRequest);

                return true;
            default:
                return false;
        }
    }

    @Override
    public void getNumFollowersFollowingSuccessful(NumFollowersFollowingResponse response) {
        followeeCount.setText("Following: " + response.getNumFollowing());
        followerCount.setText("Followers: " + response.getNumFollowers());
    }

    @Override
    public void getNumFollowersFollowingUnsuccessful(NumFollowersFollowingResponse response) {
        Toast.makeText(this, "Getting Follower/Following numbers failed." + response.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        finish();
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        Toast.makeText(this, "Logging out failed" + logoutResponse.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}