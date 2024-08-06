package com.example.taam.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taam.R;

public class FragmentLoader {

    // do not need an instance of this class
    private FragmentLoader(){}

    // allows different fragments to load other fragments
    public static void loadFragment(FragmentManager manager, Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // loads the first fragment in MainActivity
    public static void loadDefaultFragment(FragmentManager manager, Fragment fragment){
        if (manager.findFragmentById(R.id.fragment_container) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }
}
