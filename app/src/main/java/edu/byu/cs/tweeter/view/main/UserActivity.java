package edu.byu.cs.tweeter.view.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import edu.byu.cs.tweeter.R;
import model.domain.AuthToken;
import model.domain.User;
import model.service.request.FollowStateRequest;
import model.service.request.FollowUserRequest;
import model.service.request.NumFollowersFollowingRequest;
import model.service.request.UnfollowUserRequest;
import model.service.response.FollowStateResponse;
import model.service.response.FollowUserResponse;
import model.service.response.NumFollowersFollowingResponse;
import model.service.response.UnfollowUserResponse;
import edu.byu.cs.tweeter.presenter.UserPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.FollowStateTask;
import edu.byu.cs.tweeter.view.asyncTasks.FollowUserTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetNumFollowersFollowingTask;
import edu.byu.cs.tweeter.view.asyncTasks.UnfollowUserTask;
import edu.byu.cs.tweeter.view.util.ImageUtils;

public class UserActivity extends AppCompatActivity implements GetNumFollowersFollowingTask.Observer, FollowStateTask.Observer, FollowUserTask.Observer, UnfollowUserTask.Observer {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    public static final String USER_GIVEN_KEY = "UserGiven";
    public static final String MAIN_OBSERVER = "MainObserver";

    private static final String LOG_TAG = "UserActivity";

    private TextView followeeCount;
    private TextView followerCount;
    private Button followButton;

    private UserPresenter presenter;
    private GetNumFollowersFollowingTask.Observer m_mainObserver;

    private boolean isFollowing = false;
    private User m_userGiven;
    private User m_rootUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().hide();

        presenter = new UserPresenter();

        setContentView(R.layout.activity_user);

        // Grab info from preceding activity
        User user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        AuthToken authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);
        User userGiven = (User) getIntent().getSerializableExtra(USER_GIVEN_KEY);
        //m_mainObserver = (GetNumFollowersFollowingTask.Observer) getIntent().getSerializableExtra(MAIN_OBSERVER);
        m_userGiven = userGiven;
        m_rootUser = user;

        // Setup tabs
        UserSectionsPagerAdapter userSectionsPagerAdapter = new UserSectionsPagerAdapter(this, getSupportFragmentManager(), userGiven, authToken);
        ViewPager viewPager = findViewById(R.id.user_activity_view_pager);
        viewPager.setAdapter(userSectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.user_activity_tabs);
        tabs.setupWithViewPager(viewPager);

        // Assign layout objects
        TextView userName = findViewById(R.id.user_activity_userName);
        userName.setText(userGiven.getName());

        TextView userAlias = findViewById(R.id.user_activity_userAlias);
        userAlias.setText(userGiven.getAlias());

        ImageView userImageView = findViewById(R.id.user_activity_userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(userGiven.getImageBytes()));

        followeeCount = findViewById(R.id.user_activity_followeeCount);
        followerCount = findViewById(R.id.user_activity_followerCount);

        followButton = findViewById(R.id.user_activity_followButton);

        // Request Following/Follower count
        NumFollowersFollowingRequest request = new NumFollowersFollowingRequest(userGiven);
        GetNumFollowersFollowingTask getNumFollowersFollowingTask = new GetNumFollowersFollowingTask(presenter, this);
        getNumFollowersFollowingTask.execute(request);

        // Request Follow Status
        FollowStateRequest followStateRequest = new FollowStateRequest(user, userGiven);
        FollowStateTask followStateTask = new FollowStateTask(presenter, this);
        followStateTask.execute(followStateRequest);

        // Setup Observers
        FollowUserTask.Observer followObserver = this;
        UnfollowUserTask.Observer unfollowObserver = this;

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFollowing) {
                    FollowUserRequest followUserRequest = new FollowUserRequest(user, userGiven);
                    FollowUserTask followUserTask = new FollowUserTask(presenter, followObserver);
                    followUserTask.execute(followUserRequest);
                } else {
                    UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(user, userGiven);
                    UnfollowUserTask unfollowUserTask = new UnfollowUserTask(presenter, unfollowObserver);
                    unfollowUserTask.execute(unfollowUserRequest);
                }
            }
        });

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
    public void followUserSuccessful(FollowUserResponse followUserResponse) {
        changeButtonToUnfollow();
        Toast.makeText(getApplicationContext(), "You followed " + m_userGiven.getAlias(), Toast.LENGTH_SHORT).show();

        NumFollowersFollowingRequest request = new NumFollowersFollowingRequest(m_userGiven);
        GetNumFollowersFollowingTask getNumFollowersFollowingTask = new GetNumFollowersFollowingTask(presenter, this);
        getNumFollowersFollowingTask.execute(request);
    }

    @Override
    public void followUserUnsuccessful(FollowUserResponse followUserResponse) {
        Toast.makeText(getApplicationContext(), "Failed to follow user.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unfollowUserSuccessful(UnfollowUserResponse unfollowUserResponse) {
        changeButtonToFollow();
        Toast.makeText(getApplicationContext(), "You unfollowed " + m_userGiven.getAlias(), Toast.LENGTH_SHORT).show();

        NumFollowersFollowingRequest request = new NumFollowersFollowingRequest(m_userGiven);
        GetNumFollowersFollowingTask getNumFollowersFollowingTask = new GetNumFollowersFollowingTask(presenter, this);
        getNumFollowersFollowingTask.execute(request);

//        NumFollowersFollowingRequest requestMain = new NumFollowersFollowingRequest(m_rootUser);
//        GetNumFollowersFollowingTask getNumFollowersFollowingTaskMain = new GetNumFollowersFollowingTask(presenter, m_mainObserver);
//        getNumFollowersFollowingTaskMain.execute(requestMain);
    }

    @Override
    public void unfollowUserUnsuccessful(UnfollowUserResponse unfollowUserResponse) {
        Toast.makeText(getApplicationContext(), "Failed to unfollow user.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void followStateSuccessful(FollowStateResponse followStateResponse) {
        if (followStateResponse.getIsFollowing()) changeButtonToUnfollow();
        //else changeButtonToUnfollow();
    }

    @Override
    public void followStateUnsuccessful(FollowStateResponse followStateResponse) {
        Toast.makeText(getApplicationContext(), "Failed to get follow state.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void changeButtonToFollow() {
        isFollowing = false;
        followButton.setText("Follow");

        Drawable buttonDraw = followButton.getBackground();
        buttonDraw = DrawableCompat.wrap(buttonDraw);
        DrawableCompat.setTint(buttonDraw, getColor(R.color.green));
        followButton.setBackground(buttonDraw);
    }

    private void changeButtonToUnfollow() {
        isFollowing = true;
        followButton.setText("Unfollow");

        Drawable buttonDraw = followButton.getBackground();
        buttonDraw = DrawableCompat.wrap(buttonDraw);
        DrawableCompat.setTint(buttonDraw, getColor(R.color.red));
        followButton.setBackground(buttonDraw);
    }
}
