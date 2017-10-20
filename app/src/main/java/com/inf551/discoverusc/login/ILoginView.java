package com.inf551.discoverusc.login;

/**
 * Created by liwanjin on 17/9/28.
 */

public interface ILoginView {
    void showProgress(boolean enable);//When Presenter uses loginToServer，inform View to load the animation
    void showLoginView();//When log succeed, Presenter informs View.
    void showFirebaseAuthenticationFailedMessage();

}
