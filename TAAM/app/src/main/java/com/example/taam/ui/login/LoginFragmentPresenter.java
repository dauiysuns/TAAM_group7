package com.example.taam.ui.login;

import android.util.Log;

public class LoginFragmentPresenter {
    private final LoginFragmentView view;
    private final LoginFragmentModel login_fragment_model;

    public LoginFragmentPresenter(LoginFragmentView view) {
        this.view = view;
        this.login_fragment_model = new LoginFragmentModel();
    }

    public void tryLogin(String email, String password) {
        //Request from Model
        login_fragment_model.modelLoginAttempt(email, password, new LoginContract.Presenter() {
            // Call from View based on result of Model
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
        });
        Log.d("LoginFragmentPresenter", "Attempting to login with email: " + email);
    }
}