package com.inf551.discoverusc.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.inf551.discoverusc.R;
import com.inf551.discoverusc.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liwanjin on 17/9/28.
 */

public class LoginActivity extends AppCompatActivity implements ILoginView, GoogleApiClient.OnConnectionFailedListener {


    @BindView(R.id.sign_in_button)
    SignInButton sign_in_button;

    private ILoginPresenter loginPresenter;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private int SIGN_IN_REQUEST_CODE = 888;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //find view
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ButterKnife.bind(this);

        loginPresenter = new LoginPresenter(this);


        sign_in_button = (SignInButton) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE);            }
        });

        //Sign in With Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();//

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(LoginActivity.this, "Google Login Error...", Toast.LENGTH_LONG).show();

                    }
                }
        ).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    public void logOutWithFirebase(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(LoginActivity.this,"Sign Out!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                loginPresenter.logInWithFirebase(result.getSignInAccount());
            }
        }
    }
    @Override
    public void showProgress(boolean enable) {
        if(enable){
            progressBar.setVisibility(View.VISIBLE);
            sign_in_button.setVisibility(View.GONE);

        }else {
            progressBar.setVisibility(View.GONE);
            sign_in_button.setVisibility(View.VISIBLE);
      }

    }

    @Override
    public void showLoginView(){
        Toast.makeText(LoginActivity.this,"Success!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }//   When log succeed, Presenter informs View.


    @Override
    public void onStart() {
        super.onStart();
        loginPresenter.setAuthListener();
//        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        loginPresenter.removeAuthListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConntectionFailed " + connectionResult.getErrorMessage());
    }


    @Override
    public void showFirebaseAuthenticationFailedMessage() {
        Toast.makeText(LoginActivity.this, "Authentication failed.",
                Toast.LENGTH_SHORT).show();
        showProgress(false);
    }
}
