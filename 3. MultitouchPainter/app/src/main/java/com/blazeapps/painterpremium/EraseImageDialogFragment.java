package com.blazeapps.painterpremium;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;

public class EraseImageDialogFragment extends BaseDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        drawView = ((MainActivity)getActivity()).getDrawView();
        builder.setMessage(R.string.message_erase);
        builder.setPositiveButton(R.string.button_erase, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawView.clear();
            }
        });
        builder.setNegativeButton(android.R.string.cancel,null);
        return builder.create();

    }

}
