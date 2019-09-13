package com.mta.sadna19.sadna;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class NotificationDialog extends AppCompatDialogFragment {

    SwitchCompat switchCompat;
    OnPrivacyPolicyClicked mOnPrivacyPolicyClicked;

    public interface OnPrivacyPolicyClicked{
         void OnApprovedPrivacyChanges();
    }

    public void SetOnPrivacyPolicyClicked(OnPrivacyPolicyClicked onPrivacyPolicyClicked)
    {
        mOnPrivacyPolicyClicked = onPrivacyPolicyClicked;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = getArguments().getString("message");
        switchCompat = getActivity().findViewById(R.id.btnSwitchPrivacy);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("האם אתה בטוח?")
                .setMessage(message)
                .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mOnPrivacyPolicyClicked!=null)
                            mOnPrivacyPolicyClicked.OnApprovedPrivacyChanges();
                    }
                })
                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchCompat.toggle();
                    }
                });
        return  builder.create();
    }

}
