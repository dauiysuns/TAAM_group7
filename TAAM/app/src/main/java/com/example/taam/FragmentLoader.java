package com.example.taam;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentLoader {

    // do not need an instance of this class
    private FragmentLoader(){}

    public static void loadFragment(FragmentManager manager, Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void loadDefaultFragment(FragmentManager manager, Fragment fragment){
        if (manager.findFragmentById(R.id.fragment_container) == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }
}
