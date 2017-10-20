package com.inf551.discoverusc.login;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by liwanjin on 17/9/28.
 */

public interface ILoginPresenter {

    void loginSucceed();//When log succeed／fail, Model informs the event over.

    void logInWithFirebase(GoogleSignInAccount account);

    void setAuthListener();

    void removeAuthListener();

}
