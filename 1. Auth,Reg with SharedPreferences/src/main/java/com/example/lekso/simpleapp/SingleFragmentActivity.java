package com.example.lekso.simpleapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_single_fragment);
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, getFragment()).commit();

        }

    }

    protected abstract android.support.v4.app.Fragment getFragment();

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount()==1){
            finish();
        }
        else{
            fragmentManager.popBackStack();
        }
    }
}
