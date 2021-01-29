package edu.byu.cs.tweeter.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import edu.byu.cs.tweeter.R;
import model.service.request.SignupRequest;
import model.service.response.SignupResponse;
import edu.byu.cs.tweeter.presenter.SignupPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.SignupTask;
import edu.byu.cs.tweeter.view.main.MainActivity;

public class SignupActivity extends AppCompatActivity implements SignupPresenter.View, SignupTask.Observer {

    private static final String LOG_TAG = "SignupActivity";
    private static final int CAMERA_PIC_REQUEST = 123;

    private SignupPresenter presenter;
    private Toast signupToast;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Bitmap profilePic;
    private byte [] profilePicByteArray;

    private ImageView checkmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        presenter = new SignupPresenter(this);

        EditText firstNameEditText = findViewById(R.id.signUpFirstNameEditText);
        EditText lastNameEditText = findViewById(R.id.signUpLastNameEditText);
        EditText usernameEditText = findViewById(R.id.signUpUsernameEditText);
        EditText passwordEditText = findViewById(R.id.signUpPasswordEditText);
        Button signUpButton = findViewById(R.id.signUpButton);
        Button uploadPictureButton = findViewById(R.id.signUpTakePicture);
        checkmark = findViewById(R.id.signUpCheckmarkImage);

        firstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                firstName = s.toString();
            }
        });

        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lastName = s.toString();
            }
        });

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }
        });

        uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupToast = Toast.makeText(SignupActivity.this, "Signing Up", Toast.LENGTH_LONG);
                signupToast.show();

                SignupRequest signupRequest = new SignupRequest(firstName, lastName, username, password, profilePicByteArray);
                SignupTask signupTask = new SignupTask(presenter, SignupActivity.this);
                signupTask.execute(signupRequest);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            profilePic = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            profilePic.compress(Bitmap.CompressFormat.PNG, 100, stream);
            profilePicByteArray = stream.toByteArray();
            profilePic.recycle();
            checkmark.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void signupSuccessful(SignupResponse signupResponse) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra(MainActivity.CURRENT_USER_KEY, signupResponse.getUser());
        intent.putExtra(MainActivity.AUTH_TOKEN_KEY, signupResponse.getAuthToken());

        signupToast.cancel();
        startActivity(intent);
        finish();
    }

    @Override
    public void signupUnsuccessful(SignupResponse signupResponse) {
        Toast.makeText(this, "Failed to sign up. " + signupResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);
        Toast.makeText(this, "Failed to sign up because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }
}
