package com.blazeapps.painterpremium;

import android.content.Context;
import android.support.v4.app.DialogFragment;

public class BaseDialogFragment extends DialogFragment {

    protected DrawView drawView;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null) {
            ((MainActivity)getActivity()).setDialogOnScreen(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null) {
            ((MainActivity)getActivity()).setDialogOnScreen(false);
        }
    }
}
