package com.example.lekso.simpleapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationFragment extends android.support.v4.app.Fragment {


    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private Button btnRegister;
    private SharedPreferencesHelper sharedPreferencesHelper;


    public static RegistrationFragment newInstance() {

        Bundle args = new Bundle();

        RegistrationFragment fragment = new RegistrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(etEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        String pass = etPassword.getText().toString();
        String passAgain = etPasswordAgain.getText().toString();
        return pass.equals(passAgain) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(passAgain);
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fr_registration, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPass);
        etPasswordAgain = view.findViewById(R.id.etPassAgain);
        btnRegister = view.findViewById(R.id.btnRegistration);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailValid() && isPasswordValid()) {
                    boolean isAdded = sharedPreferencesHelper.addUser(new User(
                            etEmail.getText().toString(),
                            etPassword.getText().toString()
                    ));

                    if (isAdded) {
                        showMessage(R.string.registration_done);
                        getFragmentManager().popBackStack();
                    }
                    else {
                        showMessage(R.string.registration_error);
                    }
                } else {
                    showMessage(R.string.incorrect_input);
                }
            }
        });

        return view;


    }
}
