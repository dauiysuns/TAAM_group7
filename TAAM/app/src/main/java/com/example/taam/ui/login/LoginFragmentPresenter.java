package com.example.taam.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taam.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragmentPresenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragmentPresenter {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragmentPresenter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragmentPresenter.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragmentPresenter newInstance(String param1, String param2) {
        LoginFragmentPresenter fragment = new LoginFragmentPresenter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        return fragment;
    }
}