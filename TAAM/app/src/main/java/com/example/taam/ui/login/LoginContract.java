package com.example.taam.ui.login;

public interface LoginContract {
     interface Presenter {
         void tryLogin(String email, String password);
         void loginSuccessPresenter();
         void loginFailurePresenter(String error);
         void emptyEmailPresenter();
         void emptyPasswordPresenter();
    }

    interface Model {
        void modelLoginAttempt(String user, String email, Presenter presenter);
    }

    interface View {
        void loginSuccessView();
        void loginFailureView();
        void invalidInputView();
    }
}