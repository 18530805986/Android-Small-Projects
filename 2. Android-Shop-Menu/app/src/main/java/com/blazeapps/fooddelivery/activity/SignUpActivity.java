package com.blazeapps.fooddelivery.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import com.blazeapps.fooddelivery.App;
import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.etPassword)
    MaterialEditText etPassword;
    @BindView(R.id.etPhone)
    MaterialEditText etPhone;
    @BindView(R.id.etName)
    MaterialEditText etName;

    private DatabaseReference table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        //Init Firebase
        table = FirebaseDatabase.getInstance().getReference("User");

    }

    @OnClick(R.id.btnSignUp)
    void signUp(final View view) {
        final String phone = etPhone.getText().toString();
        if(!Patterns.PHONE.matcher(phone).matches()){
            Snackbar.make(view, "wrong phone number format", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
        final ProgressDialog mDialog = App.createLoadingDialog(SignUpActivity.this);
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                //check if user phone already in DB
                if (dataSnapshot.child(phone).exists()) {
                    Snackbar.make(view, "Phone number is already registered", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    User user = new User(etName.getText().toString(), etPassword.getText().toString());
                    table.child(etPhone.getText().toString()).setValue(user);
                    Snackbar.make(view, "Sign Up successfully!", Snackbar.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mDialog.dismiss();
                Snackbar.make(view, "connection failed", Snackbar.LENGTH_LONG).show();
            }
        });

    }
}
