package com.blazeapps.fooddelivery;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class App extends Application {

    private static Context context;

    public static ProgressDialog createLoadingDialog(Activity activity){
        final ProgressDialog mDialog = new ProgressDialog(activity);
        mDialog.setMessage(activity.getString(R.string.loading));
        mDialog.setCancelable(false);
        return mDialog;
    }


    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }
}
