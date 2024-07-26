package com.example.taam.ui.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class LoginFragmentModel extends Fragment{
    private final FirebaseAuth firebaseAuth;

    public LoginFragmentModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void modelLoginAttempt(String email, String password, LoginContract.Presenter listener){
        if (email.isEmpty()) {
            listener.emptyEmailPresenter();
            Log.d("uh oh", "email empty bro");
        }
        else if (password.isEmpty()) {
            listener.emptyPasswordPresenter();
            Log.d("uh oh", "password empty bro");
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Test", "Success,  in database");
                            //result to presenter
                            listener.loginSuccessPresenter();
                        } else {
                            Log.d("Test", "Fail, not in database");
                            //result to presenter
                            listener.loginFailurePresenter(Objects.requireNonNull(task.getException()).getMessage());
                        }
                    }
                });
        }

    }

}