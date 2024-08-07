package com.example.taam.ui.signup;

import static com.example.taam.ui.FragmentLoader.loadFragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.taam.R;
import com.example.taam.ui.login.LoginFragmentView;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {
    private EditText emailText;
    private EditText passwordText;

    public SignupFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        Button signup_button = view.findViewById(R.id.signup);
        emailText = view.findViewById(R.id.email_text);
        passwordText = view.findViewById(R.id.password_text);

        signup_button.setOnClickListener(view1 -> signup());

        return view;
    }

    private void signup() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Unfilled fields!", Toast.LENGTH_SHORT).show();
        }
        else {
            createUser(email, password);
        }
    }

    private void createUser(String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Create a new user with email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign-up successful!
                        Toast.makeText(getActivity(), "Sign-up successful!", Toast.LENGTH_SHORT).show();
                        loadFragment(getParentFragmentManager(), new LoginFragmentView());
                    } else {
                        // Sign-up failed
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Sign-up Failed")
                                .setMessage("Error: " + errorMessage)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                });
    }
}