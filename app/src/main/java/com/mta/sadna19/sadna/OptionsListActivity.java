package com.mta.sadna19.sadna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mta.sadna19.sadna.Adapter.OptionAdapter;
import com.mta.sadna19.sadna.MenuRegisters.DataOption;
import com.mta.sadna19.sadna.MenuRegisters.MenuFactory.Factory;
import com.mta.sadna19.sadna.MenuRegisters.Option;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OptionsListActivity extends AppCompatActivity {

    ListView m_OptionList;
    Map<String, ServiceItem> m_userFavoritesMap;
    OptionAdapter m_OptionAdapter;
    private LogicSystem m_logic;
    ImageView m_serviceImage;
    TextView m_menuName;
    ServiceItem mCurrService;
    ServerHandler mServerHandler;
    Button buttonReport, buttonContinueToServices;
    private static final int REQUEST_CODE = 1;
    String m_phoneToDial;
    private FirebaseUser fbUser;
    String LastCallpathName = "";
    String LastCallClickedItem = "";
    User currentUser;
    boolean disableBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_list);
        init();
    }

    public Option testFactory() {
        Factory factory = new Factory();
        return factory.CreateOption("1800201229", "בזק")
                .AddSubMenu(
                        factory.CreateOption("1", "לקוח קיים").AddSubMenu(
                                factory.CreateDataOption("אנא הזני מספר טלפון", "PHONE", "#").AddSubMenu(
                                        factory.CreateOption("1", "שירות לקוחות"),
                                        factory.CreateOption("2", "תמיכה טכנית")
                                )
                        ),
                        factory.CreateOption("2", "לקוח חדש")).GetOption();
    }

    private void init() {
        mServerHandler = new ServerHandler();
        disableBackButton = false;
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        buttonReport = findViewById(R.id.btnReportOnProblem);
        if (fbUser == null)
            buttonReport.setVisibility(View.GONE);
        buttonContinueToServices = findViewById(R.id.btnToServices);
        buttonContinueToServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsListActivity.this, MenuListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write a report
                buttonReport.setVisibility(View.GONE);
                openReportProblemDialog();

            }
        });
        this.m_OptionList = (ListView) findViewById(R.id.optionList);
        Option option = getIntent().getParcelableExtra("OPTION_SELECTED");
        mCurrService = getIntent().getParcelableExtra("service");
        m_menuName = findViewById(R.id.menuName);
        m_serviceImage = findViewById(R.id.serviceImage);
        m_menuName.setText(mCurrService.getM_name());
        String url = mCurrService.getM_avatar();
        Picasso.get().load(url).into(m_serviceImage);
        this.m_OptionAdapter = new OptionAdapter(getApplicationContext(), option);
        m_OptionList.setAdapter(m_OptionAdapter);
        m_logic = new LogicSystem(option);

        m_OptionAdapter.setOnOptionSelected(new OptionAdapter.onOptionSelectedListener() {
            @Override
            public void onItemSelected(Option i_op) {
                if (i_op.getType().equals("Option")) {
                    if (i_op.getName() != null) {
                        LastCallClickedItem = i_op.getName();
                        LastCallpathName = LastCallpathName + "/" + i_op.getName();
                    }

                }

                m_logic.SelectedOption(i_op);
            }
        });
        m_logic.setOnOptionSelectedListener(new LogicSystem.SelectedListener() {
            @Override
            public void onOptionSelected(Option i_op) {
                switch (i_op.getType()) {
                    case "DataOption":
                        DataOption dataOpt = (DataOption) i_op;
                        if (fbUser != null)
                            mServerHandler.writeUserAttribute(dataOpt.getDataType(), dataOpt.getKeys());
                        break;
                }
                m_OptionAdapter.updateAdapter(i_op);
                m_OptionAdapter.notifyDataSetChanged();
            }
        });
        m_logic.setOnBackSelectedListener(new LogicSystem.BackListener() {
            @Override
            public void onBackSelected() {
                m_OptionAdapter.updateAdapter(m_logic.GetLastOption());
                m_OptionAdapter.notifyDataSetChanged();
            }
        });
        m_logic.setOnLastOptionListener(new LogicSystem.LastOptionListener() {
            @Override
            public void onLastOption() {
                String allPressedKeys = m_logic.GetAllKeysString();
                //Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + allPressedKeys));
                m_phoneToDial = allPressedKeys;
                //======================================================================

                makeCall(allPressedKeys);

                onAuthenticatedUserCalled();
                disableBackButton = true;
                //======================================================================
                if (fbUser != null)
                    buttonReport.setVisibility(View.VISIBLE);
                buttonContinueToServices.setVisibility(View.VISIBLE);

                //startActivity(intent);
                //finish();
            }
        });
        if (mServerHandler.IsUserLogedIn()) {

            currentUser = new User();
            currentUser.setM_PrivacyPolicy(false);
            if (fbUser != null) {
                mServerHandler.SetOnFavoritesServicesFetchedListener(new ServerHandler.onFavoritesServicesFetchedListener() {
                    @Override
                    public void OnFavoritesServicesFetched(Map<String, ServiceItem> i_favoritesServicesArray) {

                        m_userFavoritesMap = new HashMap<>(i_favoritesServicesArray);
                    }
                });
                mServerHandler.fetchUserFavoritesServices();

            }
            mServerHandler.SetOnUserFetchedListener(new ServerHandler.OnUserFetchedListener() {
                @Override
                public void OnUserFetch(User i_user) {
                    currentUser = i_user;
                }
            });
            mServerHandler.fetchUser(fbUser.getUid());


            m_OptionAdapter.setOnOptionViewCreatedListener(new OptionAdapter.OnOptionViewCreatedListener() {
                @Override
                public void OnOptionViewCreated(OptionAdapter.OptionViewHolder iViewHolder) {
                    if (mServerHandler.getmUser().isM_PrivacyPolicy()) {
                        switch (iViewHolder.getOption().getType()) {

                            case "DataOption":
                                Log.e("$testt$", "case DataOption");
                                final OptionAdapter.DataOptionViewHolder vh = (OptionAdapter.DataOptionViewHolder) iViewHolder;
                                mServerHandler.SetOnAttributeFetchedListener(new ServerHandler.OnAttributeFetchedListener() {
                                    @Override
                                    public void OnAtttibuteFetch(String i_attributeValue) {
                                        Log.e("$testt$", "OnAtttibuteFetch" + i_attributeValue);
                                        vh.SetDataText(i_attributeValue);
                                    }
                                });
                                Log.e("$testt$", "fetchUserAttribute " + ((DataOption) vh.getOption()).getDataType());
                                mServerHandler.fetchUserAttribute(((DataOption) vh.getOption()).getDataType());
                                break;
                        }
                    }

                }
            });
        }
    }


    private void onAuthenticatedUserCalled() {

        if (fbUser != null) {
            mServerHandler.writeUserFavoriteServices(addServiceToFavoriteList());
            mServerHandler.writeUserLastCall(LastCallClickedItem, m_phoneToDial);
        }
    }

    private Map<String, ServiceItem> addServiceToFavoriteList() {
        Map<String, ServiceItem> newMapToUpdate = new HashMap<>();
        int newArrayIndex = 2;
        int i = 1;
        for (i = 1; i < 6; i++) {
            if (m_userFavoritesMap.containsKey(String.valueOf(i))) {
                if (m_userFavoritesMap.get(String.valueOf(i)).getM_name().equals(mCurrService.getM_name()))
                    newArrayIndex--;
                else
                    newMapToUpdate.put(String.valueOf(newArrayIndex), m_userFavoritesMap.get(String.valueOf(i)));
            } else {
                break;
            }
            newArrayIndex++;
        }
        newMapToUpdate.put("1", mCurrService);
        return newMapToUpdate;
    }

    @Override
    public void onBackPressed() {
        if (!m_logic.isBackToServices()&&!disableBackButton)
            m_logic.Back();
        else {
            super.onBackPressed();
        }
    }


    private void makeCall(String i_number) {

            //make call
            String phoneToDial = "tel:" + i_number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(phoneToDial)));
    }

    private void openReportProblemDialog() {

        Bundle bundle = new Bundle();
        bundle.putString("service_name", mCurrService.getM_name());
        bundle.putString("last_call", LastCallClickedItem);
        bundle.putString("service_avatar", mCurrService.getM_avatar());
        bundle.putString("fullPath", LastCallpathName);
        bundle.putString("dialPath", m_phoneToDial);


        ReportProblemDialog dialog = new ReportProblemDialog();
        dialog.SetOnSendReportClicked(new ReportProblemDialog.OnSendReportClicked() {
            @Override
            public void OnSendReport(MenuProblem i_menuProblem) {
                mServerHandler.addPointsToUser(50+currentUser.getM_points());
                mServerHandler.writeAProblem(i_menuProblem);
                Intent intent = new Intent(OptionsListActivity.this, MenuListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "");
    }


}
