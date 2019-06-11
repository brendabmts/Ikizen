package com.example.ikigaiapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ApagarCriaImageDialogFragment extends DialogFragment {

    private CriarIkigai getDoodleFragment (){
        return (CriarIkigai)
                (getFragmentManager().findFragmentById(R.id.main2Fragment));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        CriarIkigai fragment = getDoodleFragment();
        if (fragment != null){
            fragment.setDialogOnScreen(true);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        CriarIkigai fragment = getDoodleFragment();
        if (fragment != null){
            fragment.setDialogOnScreen(false);
        }
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_erase);
        builder.setPositiveButton(R.string.button_erase, new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDoodleFragment().getDoodleView().clear();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }
}
