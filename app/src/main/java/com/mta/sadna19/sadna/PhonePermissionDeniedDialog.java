package com.mta.sadna19.sadna;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

public class PhonePermissionDeniedDialog extends AppCompatDialogFragment {


    OnPermissionGrantedAfterAll monPermittionGrantedAfterAll;
    public interface OnPermissionGrantedAfterAll {
        public void onAskPermitionAgain();
    }
    public void SetOnPermittionGrantedAfterAll(OnPermissionGrantedAfterAll i_OnPermissionGrantedAfterAll)
    {
        monPermittionGrantedAfterAll = i_OnPermissionGrantedAfterAll;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("האם אתה בטוח?")
                .setMessage("על מנת להינות מהאפליקציה עליך לאשר ביצוע שיחות במכשיר")
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (monPermittionGrantedAfterAll!=null)
                            monPermittionGrantedAfterAll.onAskPermitionAgain();
                    }
                })
                .setNegativeButton("מעדיף שלא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
        return  builder.create();
    }



}
