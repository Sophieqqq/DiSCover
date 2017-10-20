package com.inf551.discoverusc.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.content.ContentValues.TAG;

/**
 * Created by liwanjin on 17/9/28.
 */

public class LoginPresenter implements ILoginPresenter{

    private ILoginView loginView;

    public FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "User is Signed In");
                loginView.showLoginView();
            } else {
                Log.d(TAG, "User is Signed Out");
            }
        }
    };
    public void setAuthListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void removeAuthListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    public LoginPresenter(ILoginView loginView){
        this.loginView = loginView;
        mFirebaseAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void loginSucceed() {
        loginView.showProgress(false);//Hide animation

        loginView.showLoginView();//Inform View the success
    }


    @Override
    public void logInWithFirebase(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            loginView.showFirebaseAuthenticationFailedMessage();

                        } else {
                            Log.d(TAG, "signInWithCredential:success");
                            loginSucceed();

                        }
                    }
                });
    }




}
