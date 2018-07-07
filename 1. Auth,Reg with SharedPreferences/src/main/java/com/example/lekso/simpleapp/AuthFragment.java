package com.example.lekso.simpleapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView etLogin;
    private EditText etPassword;
    private Button btnEnter;
    private Button btnRegister;
    private SharedPreferencesHelper sharedPreferencesHelper;

    private ArrayAdapter<String> loggedUsersAdapter;

    public static AuthFragment newInstance() {

        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(etLogin.getText())
                && Patterns.EMAIL_ADDRESS.matcher(etLogin.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(etPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {
                User user = sharedPreferencesHelper.login(
                        etLogin.getText().toString(),
                        etPassword.getText().toString());
                if (user != null) {
                    Intent startProfileIntent = new Intent(getActivity(), ProfileActivity.class);
                    startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                    startActivity(startProfileIntent);
                    getActivity().finish();
                } else {
                    showMessage(R.string.invalid_login_password);
                }
            } else {
                showMessage(R.string.incorrect_input);
            }
        }
    };

    private View.OnFocusChangeListener onLoginFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            etLogin.showDropDown();
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fr_auth, container, false);
        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        etLogin = view.findViewById(R.id.etLogin);
        etPassword = view.findViewById(R.id.etPassword);
        btnEnter = view.findViewById(R.id.buttonEnter);
        btnRegister = view.findViewById(R.id.buttonRegister);

        btnEnter.setOnClickListener(onEnterClickListener);
        etLogin.setOnFocusChangeListener(onLoginFocusChangeListener);

        btnRegister.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().
                        replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                        .addToBackStack(RegistrationFragment.class.getName()).commit();
            }
        });

        loggedUsersAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                sharedPreferencesHelper.getSuccessLogins()
        );
        etLogin.setAdapter(loggedUsersAdapter);

        return view;

    }
}
