package com.example.lekso.simpleapp;

import android.support.v4.app.Fragment;

public class AuthActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }
}
