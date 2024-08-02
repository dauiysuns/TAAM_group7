package com.example.taam.ui.login;

import static com.example.taam.ui.FragmentLoader.loadFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.taam.R;
import com.example.taam.ui.home.AdminHomeFragment;
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

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        Button loginButton = view.findViewById(R.id.signup);
        Button signupButton = view.findViewById(R.id.go_signup);
        EditText emailText = view.findViewById(R.id.email_text);
        EditText passwordText = view.findViewById(R.id.password_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailText.getText().toString().trim();
                String password = passwordText.getText().toString();
                //ask presenter!
                presenter.tryLogin(email, password);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // THIS CAN USE THE FRAGMENT LOADER THING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                loadFragment(getParentFragmentManager(), new SignupFragment());
            }
        });

        return view;
    }

    // These methods are called by Presenter after model looks at the database
    public void loginSuccessView() {
        Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
        loadFragment(getParentFragmentManager(), new AdminHomeFragment());
    }

    public void loginFailureView() {
        Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
        Log.d("failure", "logged in not good");
    }

    public void invalidInputView() {
        Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
    }
}