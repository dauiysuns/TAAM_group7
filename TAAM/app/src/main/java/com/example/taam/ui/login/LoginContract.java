package com.example.taam.ui.login;

public interface LoginContract {
     interface Presenter {
         void loginSuccessPresenter();
         void loginFailurePresenter(String error);
         void emptyEmailPresenter();
         void emptyPasswordPresenter();
    }

    interface Model {
        interface loginFinishedListener {
            void onUsernameError(String error);
            void onPasswordError(String error);
            void isSuccess();
            void isFailure(String error);
        }
    }

    interface View {
        void loginSuccessView();
        void loginFailureView(String error);
        void invalidInputView();
    }
}