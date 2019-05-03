package com.blazeapps.painterpremium;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

public class LineWidthDialogFragment extends BaseDialogFragment {

    private ImageView widthImageView;
    private SeekBar widthSeekBar;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View lineWidthDialogView = getActivity().getLayoutInflater().inflate(R.layout.fragment_line_with, null);
        builder.setView(lineWidthDialogView);

        builder.setTitle(R.string.title_line_width_dialog);
        widthImageView = lineWidthDialogView.findViewById(R.id.widthImageView);

        drawView = ((MainActivity)getActivity()).getDrawView();
        widthSeekBar = lineWidthDialogView.findViewById(R.id.widthSeekBar);
        widthSeekBar.setOnSeekBarChangeListener(widthChangeListener);
        widthSeekBar.setProgress(drawView.getLineWidth());

        builder.setPositiveButton(R.string.button_set_line_width, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drawView.setLineWidth(widthSeekBar.getProgress());
            }
        });
        return builder.create();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        drawView.setLineWidth(widthSeekBar.getProgress());
    }

    private final SeekBar.OnSeekBarChangeListener widthChangeListener = new SeekBar.OnSeekBarChangeListener() {

        final Bitmap bitmap = Bitmap.createBitmap(400, 100, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            Paint p = new Paint();
            p.setColor(drawView.getDrawingColor());
            p.setStrokeCap(Paint.Cap.ROUND);
            p.setStrokeWidth(i);
            if (Build.VERSION.SDK_INT >= 23) {
                bitmap.eraseColor(getResources().getColor(android.R.color.transparent, getContext().getTheme()));
            }
            else {
                bitmap.eraseColor(getResources().getColor(android.R.color.transparent));
            }
            canvas.drawLine(30,50,370,50,p);
            widthImageView.setImageBitmap(bitmap);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}

