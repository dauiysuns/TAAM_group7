package com.example.taam.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.taam.R;
import com.example.taam.ui.signup.SignupFragment;

public class LoginFragmentView extends Fragment {
    private LoginFragmentPresenter presenter;

    public LoginFragmentView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginFragmentPresenter(this);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button login_button = view.findViewById(R.id.signup);
        Button signup_button = view.findViewById(R.id.go_signup);
        EditText emailText = view.findViewById(R.id.emailText);
        EditText passwordText = view.findViewById(R.id.passwordText);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString();
                //ask presenter!
                presenter.tryLogin(email, password);
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // THIS CAN USE THE FRAGMENT LOADER THING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                SignupFragment signupFragment = new SignupFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, signupFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    // These methods are called by Presenter after model looks at the database
    public void loginSuccessView() {
        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
        // NEW ACTIVITY AFTER A SUCCESSFUL LOGIN!!
//        Intent intent = new Intent(getActivity(), UserLoggedInActivity.class);
//        startActivity(intent);
    }

    public void loginFailureView() {
        Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
        Log.d("failure", "logged in not good");
    }

    public void invalidInputView() {
        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
    }
}