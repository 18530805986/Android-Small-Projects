package com.blazeapps.fooddelivery.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;

import com.blazeapps.fooddelivery.App;
import com.blazeapps.fooddelivery.R;
import com.blazeapps.fooddelivery.common.Common;
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

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.etPassword)
    MaterialEditText etPassword;
    @BindView(R.id.etPhone)
    MaterialEditText etPhone;

    private DatabaseReference table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        //Init Firebase
        table = FirebaseDatabase.getInstance().getReference("User");
    }

    @OnClick(R.id.btnSignIn)
    void singIn(final View view) {
        final ProgressDialog mDialog = App.createLoadingDialog(SignInActivity.this);
        mDialog.show();
        table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDialog.dismiss();
                //check if user exists
                String phone = etPhone.getText().toString();
                if (Patterns.PHONE.matcher(phone).matches() && dataSnapshot.child(phone).exists()) {
                    //get user info
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(etPassword.getText().toString())) {
                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Common.currentUser = user;
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(view, "Wrong password!", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, "User not exist", Snackbar.LENGTH_LONG).show();

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
