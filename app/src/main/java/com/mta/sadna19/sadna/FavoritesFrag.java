package com.mta.sadna19.sadna;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mta.sadna19.sadna.Adapter.MenuAdapter;
import com.mta.sadna19.sadna.MenuRegisters.Option;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mta.sadna19.sadna.MenuListActivity.PReqCode;
import static com.mta.sadna19.sadna.MenuListActivity.REQUESTCODE;

public class FavoritesFrag extends Fragment {
    ArrayList<ServiceItem> mFavoritesArray;
    ServerHandler mServerHandler;
    RecyclerView mRecyclerView;
    Context mContext;
    TextView tvLastCall;
    TextView tvServiceNameLastCall;
    ImageView imgLastCall;
    MenuAdapter mMenuAdapter;
    String LastCall;
    String LastCallDial;
    private static final String OPTION_SELECTED = "OPTION_SELECTED";

    private static final int REQUEST_CODE = 1;

    private static final String TAG = "onFavoritesFrag";

    OnDialLastCallClicked mOnDialLastCallClicked;

    public interface OnDialLastCallClicked {
        public void onDialLastCall();
    }

    public void SetOnDialLastCallListener(OnDialLastCallClicked onDialLastCallClicked) {
        mOnDialLastCallClicked = onDialLastCallClicked;
    }

    OnFavoritesMenuClicked mOnFavoritesMenuClicked;

    public interface OnFavoritesMenuClicked {
        public void onDialMenu();
    }

    public void SetOnFavoritesMenuClicked(OnFavoritesMenuClicked onFavoritesMenuClicked) {
        mOnFavoritesMenuClicked = onFavoritesMenuClicked;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mServerHandler = new ServerHandler();
        View fragView = inflater.inflate(R.layout.favorites_frag, null);
        mRecyclerView = fragView.findViewById(R.id.recyclerFavoritesServices);
        //imgLastCall = fragView.findViewById(R.id.imgLastCall);
        tvLastCall = fragView.findViewById(R.id.tvLastCall);
        tvServiceNameLastCall = fragView.findViewById(R.id.tvServiceNameLastCall);
        imgLastCall = fragView.findViewById(R.id.btnLastCall);
        mContext = getActivity().getApplicationContext();
        return fragView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFavoritesArray = new ArrayList<>();

        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        mServerHandler = new ServerHandler();
        mMenuAdapter = new MenuAdapter(mContext, mFavoritesArray);

        imgLastCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(LastCallDial);
            }
        });
        mMenuAdapter.setOnMenuClickListener(new MenuAdapter.OnMenuClickListener() {
            @Override
            public void OnMenuClick(ServiceItem iMenu) {
                mServerHandler.fetchMenu(iMenu.getM_name());
            }
        });
        mRecyclerView.setAdapter(mMenuAdapter);
        mServerHandler.SetOnOptionFetchedListener(new ServerHandler.OnOptionFetchedListener() {
            @Override
            public void OnMenuFetch(Option i_opt, ServiceItem i_service) {
                Intent intent = new Intent(mContext, OptionsListActivity.class);

                intent.putExtra(OPTION_SELECTED, i_opt);
                intent.putExtra("service", i_service);
                startActivity(intent);
            }
        });
        mServerHandler.SetOnLastCallFetchedListener(new ServerHandler.onLastCallFetchedListener() {
            @Override
            public void OnLastCallFetched(String i_userLastCall, String i_userLastCallDial) {
                LastCall = i_userLastCall;
                Log.e(TAG, "LastCALL: " + LastCall);
                tvLastCall.setText(LastCall);
                LastCallDial = i_userLastCallDial;

            }
        });
        mServerHandler.SetOnFavoritesServicesFetchedListener(new ServerHandler.onFavoritesServicesFetchedListener() {
            @Override
            public void OnFavoritesServicesFetched(Map<String, ServiceItem> i_favoritesServicesMap) {
                Log.e(TAG, "onFavoritesFetched");
                //mRecyclerView.setVisibility(View.VISIBLE);
                if (i_favoritesServicesMap != null)
                    initData(i_favoritesServicesMap);
                //initLastCall();
            }
        });
        /*mServerHandler.SetOnOptionFetchedListener(new ServerHandler.OnOptionFetchedListener() {
            @Override
            public void OnMenuFetch(Option i_opt, ServiceItem i_service) {
                Intent intent = new Intent(mContext, OptionsListActivity.class);

                intent.putExtra(OPTION_SELECTED, i_opt);
                intent.putExtra("service", i_service);
                startActivity(intent);
            }
        });*/
        getData();
        mServerHandler.fetchUserLastCall();

    }


    private void getData() {
        mServerHandler.fetchUserFavoritesServices();
    }

    private void initData(Map<String, ServiceItem> i_favoritesServices) {
        Log.e(TAG, i_favoritesServices.toString());
        mFavoritesArray.clear();
        for (int i = 1; i < 6; i++) {
            if (i_favoritesServices.containsKey(String.valueOf(i))) {
                ServiceItem item = i_favoritesServices.get(String.valueOf(i));
                mFavoritesArray.add(item);
                Log.e(TAG, item.toString());

            } else {
                break;
            }

        }
        initLastCall();
        mMenuAdapter.setmMenuListFull(new ArrayList<>(mFavoritesArray));
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void initLastCall() {
        ServiceItem item = new ServiceItem();
        if (mFavoritesArray.size() > 0) {
            imgLastCall.setVisibility(View.VISIBLE);
            item = mFavoritesArray.get(0);
            tvServiceNameLastCall.setText("ב" + item.getM_name());
        }
    }

    public FavoritesFrag() {

    }


    private void makeCall(String i_number) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);
            makeCall(i_number);
        } else {
            //make call
            String phoneToDial = "tel:" + i_number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(phoneToDial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(LastCallDial);
            } else {
                //permission denied
            }
        }
    }


}