package com.blazeapps.painterpremium;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;

public class ColorDialogFragment extends BaseDialogFragment {

    private SeekBar alphaSeekBar;
    private SeekBar redSeekBar;
    private SeekBar blueSeekBar;
    private SeekBar greenSeekBar;
    private View colorView;
    private int color;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View colorDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_color, null);
        builder.setView(colorDialogView);
        builder.setTitle(R.string.title_color_dialog);
        alphaSeekBar = colorDialogView.findViewById(R.id.alphaSeekBar);
        redSeekBar = colorDialogView.findViewById(R.id.redSeekBar);
        blueSeekBar = colorDialogView.findViewById(R.id.blueSeekBar);
        greenSeekBar = colorDialogView.findViewById(R.id.greenSeekBar);
        colorView = colorDialogView.findViewById(R.id.colorView);

        alphaSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        drawView = ((MainActivity)getActivity()).getDrawView();
        color = drawView.getDrawingColor();
        colorView.setBackgroundColor(color);
        alphaSeekBar.setProgress(Color.alpha(color));
        redSeekBar.setProgress(Color.red(color));
        blueSeekBar.setProgress(Color.blue(color));
        greenSeekBar.setProgress(Color.green(color));

        builder.setPositiveButton(R.string.button_set_color, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawView.setDrawingColor(color);
            }
        });
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawView.setDrawingColor(color);
    }

    private final SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b) {
                color = Color.argb(alphaSeekBar.getProgress(), redSeekBar.getProgress(),
                        greenSeekBar.getProgress(), blueSeekBar.getProgress());
                colorView.setBackgroundColor(color);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

}
