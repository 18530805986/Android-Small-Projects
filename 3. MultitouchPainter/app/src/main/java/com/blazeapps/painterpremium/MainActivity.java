package com.blazeapps.painterpremium;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ActionMenuView;

public class MainActivity extends AppCompatActivity {

    private DrawView drawView;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private boolean dialogOnScreen = false;

    private static final int ACCELERATION_THRESHOLD = 100000;
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1;

    private android.support.v7.widget.ActionMenuView actionMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionMenuView = findViewById(R.id.amvMenu);
        actionMenuView.setOnMenuItemClickListener(new android.support.v7.widget.ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        drawView = findViewById(R.id.drawView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }


    private void confirmErase() {
        EraseImageDialogFragment fragment = new EraseImageDialogFragment();
        fragment.show(getSupportFragmentManager(), "erase dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_fragment_menu, actionMenuView.getMenu());
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.color:
                ColorDialogFragment colorDialog = new ColorDialogFragment();
                colorDialog.show(getSupportFragmentManager(), "color dialog");
                return true;
            case R.id.line_width:
                LineWidthDialogFragment lineWidthDialog = new LineWidthDialogFragment();
                lineWidthDialog.show(getSupportFragmentManager(), "line width dialog");
                return true;
            case R.id.delete_drawing:
                confirmErase();
                return true;
            case R.id.save:
                saveImage();
                return true;
            case R.id.print:
                drawView.printImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.permission_explanation);
                builder.setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                requestPermissions(new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
                            }
                        }
                );
                builder.show();
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
            }

        } else {
            drawView.saveImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SAVE_IMAGE_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    drawView.saveImage();
                }
                break;
        }
    }

    public DrawView getDrawView() {
        return drawView;
    }

    public void setDialogOnScreen(boolean visible) {
        dialogOnScreen = visible;
    }

}
