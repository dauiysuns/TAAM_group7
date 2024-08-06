package com.example.taam.ui.login;

import android.util.Log;

public class LoginFragmentPresenter implements LoginContract.Presenter{
    private final LoginFragmentView view;
    private final LoginFragmentModel login_fragment_model;

    public LoginFragmentPresenter(LoginFragmentView view, LoginFragmentModel model) {
        this.view = view;
        this.login_fragment_model = model;
    }

    @Override
    public void tryLogin(String email, String password) {
        //Request from Model
        login_fragment_model.modelLoginAttempt(email, password, this);
        Log.d("LoginFragmentPresenter", "Attempting to login with email: " + email);
    }

    @Override
    public void loginSuccessPresenter() {
        view.loginSuccessView();
    }

    @Override
    public void loginFailurePresenter(String error) {
        view.loginFailureView();
    }

    @Override
    public void emptyEmailPresenter() {
        view.invalidInputView();
    }

    @Override
    public void emptyPasswordPresenter() {
        view.invalidInputView();
    }
}