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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ReportProblemDialog extends AppCompatDialogFragment {

    Button btnOption1,btnOption2,btnOption3,btnReport;
    OnSendReportClicked mOnSendReportClicked;
    TextView tvServiceName,tvLastCallItem,tvTitle,tvFreeText;
    ImageView imgService;
    ServerHandler mServerHandler;
    String fullPath,dialPath;
    MenuProblem menuProblem ;
    public interface OnSendReportClicked{
        public void OnSendReport(MenuProblem i_menuProblem);
    }

    public void SetOnSendReportClicked(OnSendReportClicked iOnSendReportClicked)
    {
        mOnSendReportClicked = iOnSendReportClicked;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.report_layout,null);
        builder.setView(view);
        //mServerHandler = new ServerHandler();
        tvFreeText = view.findViewById(R.id.tvBody);
        //tvTitle = view.findViewById(R.id.tvTitle);
        btnReport = view.findViewById(R.id.btnSendReport);
        imgService = view.findViewById(R.id.imgService);
        tvServiceName = view.findViewById(R.id.tvServiceName);
        tvLastCallItem = view.findViewById(R.id.tvLastCallItem);
        //tvLastCallItem = view.findViewById(R.id.tvServiceName);
        menuProblem = new MenuProblem();
        tvServiceName.setText(getArguments().get("service_name").toString());
        tvLastCallItem.setText(getArguments().get("last_call").toString());
        fullPath = getArguments().get("fullPath").toString();
        dialPath = getArguments().get("dialPath").toString();
        Picasso.get().load(getArguments().get("service_avatar").toString()).into(imgService);
        btnOption1 = view.findViewById(R.id.btnOption1);
        btnOption2 = view.findViewById(R.id.btnOption2);
        btnOption3 = view.findViewById(R.id.btnOption3);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init MenuProblem

                initReport();
                //mServerHandler.writeAProblem(menuProblem);
                if (mOnSendReportClicked!=null)
                    mOnSendReportClicked.OnSendReport(menuProblem);
            }
        });
        manageReportOptions();
        return builder.create();
    }



    private void manageReportOptions()
    {
        btnOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuProblem.setmClassification(btnOption1.getText().toString());
                btnOption1.setBackground(getResources().getDrawable(R.drawable.btn_style2));
                btnOption2.setBackground(getResources().getDrawable(R.drawable.btn_style3));
                btnOption3.setBackground(getResources().getDrawable(R.drawable.btn_style3));
            }
        });
        btnOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuProblem.setmClassification(btnOption2.getText().toString());

                btnOption2.setBackground(getResources().getDrawable(R.drawable.btn_style2));
                btnOption1.setBackground(getResources().getDrawable(R.drawable.btn_style3));
                btnOption3.setBackground(getResources().getDrawable(R.drawable.btn_style3));
            }
        });
        btnOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuProblem.setmClassification(btnOption3.getText().toString());
                btnOption3.setBackground(getResources().getDrawable(R.drawable.btn_style2));
                btnOption2.setBackground(getResources().getDrawable(R.drawable.btn_style3));
                btnOption1.setBackground(getResources().getDrawable(R.drawable.btn_style3));
            }
        });
    }
    private void initReport()
    {
        menuProblem.setmServiceName(tvServiceName.getText().toString());
        menuProblem.setmUserFreeText(tvFreeText.getText().toString());
        menuProblem.setmLastCallPath(fullPath);
        menuProblem.setmLastCallDialPath(dialPath);
        menuProblem.setmStatus("ON");
    }


}
