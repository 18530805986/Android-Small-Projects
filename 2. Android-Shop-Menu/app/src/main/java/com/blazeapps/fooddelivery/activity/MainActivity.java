package com.blazeapps.fooddelivery.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.blazeapps.fooddelivery.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tvSlogan)
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Nabila.ttf");
        txtSlogan.setTypeface(typeface);
    }

    @OnClick(R.id.btnSignUpGo)
    void goToSignUp() {
        Intent signUp = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(signUp);
    }

    @OnClick(R.id.btnSignInGo)
    void goToSignIn() {
        Intent signIn = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(signIn);
    }
}
