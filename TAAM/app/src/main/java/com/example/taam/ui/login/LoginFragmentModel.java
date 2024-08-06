package com.example.taam.ui.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginFragmentModel extends Fragment implements LoginContract.Model {
    private final FirebaseAuth firebaseAuth;

    public LoginFragmentModel(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void modelLoginAttempt(String email, String password, LoginContract.Presenter presenter) {
        if (email.isEmpty()) {
            presenter.emptyEmailPresenter();
            Log.d("uh oh", "email empty bro");
        } else if (password.isEmpty()) {
            presenter.emptyPasswordPresenter();
            Log.d("uh oh", "password empty bro");
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Test", "Success, in database");
                                presenter.loginSuccessPresenter();
                            } else {
                                Log.d("Test", "Fail, not in database");
                                presenter.loginFailurePresenter(Objects.requireNonNull(task.getException()).getMessage());
                            }
                        }
                    });
        }
    }

}
