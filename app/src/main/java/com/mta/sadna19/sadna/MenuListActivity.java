package com.mta.sadna19.sadna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mta.sadna19.sadna.Adapter.MenuAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static java.time.OffsetDateTime.now;

public class MenuListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String TAG = "$MenuListActivity$";
    private static final String OPTION_SELECTED = "OPTION_SELECTED";
    RecyclerView mRecyclerView;
    List<ServiceItem> mMenuList;
    ServerHandler mServerHandler;
    MenuAdapter nMenuAdapter;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;
    User signedUpUser;
    private FirebaseUser fbUser;
    private DrawerLayout drawerLayout;
    TextView tvHeaderHelloUser, tvHeaderEmailUser;
    Bundle mSavedInstanceState;
    ImageView imgHeaderProfilePic;
    ProfileFrag mProfileFrag;
    HomeFrag mHomeFrag;
    FavoritesFrag mFavoritesFrag;
    static int PReqCode = 2;
    static int REQUESTCODE = 1;
    NavigationView navView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate() >>");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        mSavedInstanceState = savedInstanceState;

        requestPer();

        init();
        Log.e(TAG, "onCreate() <<");
    }


    private void openPermissionDeniedDialog()
    {
        PhonePermissionDeniedDialog dialog = new PhonePermissionDeniedDialog();
        dialog.SetOnPermittionGrantedAfterAll(new PhonePermissionDeniedDialog.OnPermissionGrantedAfterAll() {
            @Override
            public void onAskPermitionAgain() {
                requestPer();
            }
        });
        dialog.show(getSupportFragmentManager(),"");
    }
    private void requestPer()
    {

        if (ContextCompat.checkSelfPermission(MenuListActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuListActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUESTCODE);
            Log.e(TAG,"onRequestPer granted");
        } else {
            //make call
            Log.e(TAG,"onRequestPer denied");

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUESTCODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"onpermissionResult granted");
            } else {
                //do something else

                openPermissionDeniedDialog();
                Log.e(TAG,"onpermissionResult denied");

            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.e(TAG, "onNavigationItemSelected");
        switch (menuItem.getItemId()) {
            case R.id.nav_profile: {
                mProfileFrag.SetOnImageUpdateClickListener(new ProfileFrag.onProfileImageUpdate() {
                    @Override
                    public void onProfileImageUpdate(Uri i_uri) {
                        Picasso.get().load(i_uri).transform(new CircleTransform()).into(imgHeaderProfilePic);

                    }
                });
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mProfileFrag).addToBackStack(null).commit();

                break;
            }
            case R.id.nav_home: {
                //HomeFrag homeFrag = new HomeFrag();
                mHomeFrag.initHomeFrag(getApplicationContext(), signedUpUser);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mHomeFrag).commit();
                break;
            }

            case R.id.nav_favorites: {
                //HomeFrag homeFrag = new HomeFrag();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFavoritesFrag).addToBackStack(null).commit();
                break;
            }

            case R.id.nav_log_in: {
                //Intent intent = new Intent(MenuListActivity.this, signinActivity.class);
                Intent intent = new Intent(MenuListActivity.this, corridorActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.nav_log_out: {
                signOutClicked();
                break;
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void init() {
        Log.e(TAG, "init() >>");
        mServerHandler = new ServerHandler();
        mHomeFrag = new HomeFrag();
        mFavoritesFrag = new FavoritesFrag();
        mProfileFrag = new ProfileFrag();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();

        drawerManager();
        authManager();

    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void signOutClicked() {
        mAuth.signOut();
        //LoginManager.getInstance().logOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

        startActivity(new Intent(MenuListActivity.this, corridorActivity.class));
        finish();
    }


    private void initNavigatorHeader() {

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        tvHeaderHelloUser = (TextView) header.findViewById(R.id.tvHeader_hello_user);
        tvHeaderEmailUser = (TextView) header.findViewById(R.id.tvHeader_email);

        imgHeaderProfilePic = header.findViewById(R.id.imgHeader);
        headerMessageByTime();
        tvHeaderHelloUser.setText("שלום ");
        if (fbUser != null) {
            if (fbUser.getPhotoUrl() != null) {
                if (fbUser.getPhotoUrl() != null && imgHeaderProfilePic != null) {
                    Picasso.get().load(fbUser.getPhotoUrl()).transform(new CircleTransform()).into(imgHeaderProfilePic);
                } else {
                    imgHeaderProfilePic.setImageResource(R.drawable.ic_launcher_foreground);
                }
            }

            //tvHeaderEmailUser.setText(signedUpUser.getM_email());
            String user_name = signedUpUser.getM_name();
            if (!(user_name == null || user_name.isEmpty()))
                tvHeaderHelloUser.setText("שלום " + signedUpUser.getM_name());

        } else {
            //tvHeaderEmailUser.setText("");
        }

    }

    private void headerMessageByTime()
    {
        String timeStamp = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        Log.e(TAG,"time is: "+timeStamp);
        int hour = Integer.parseInt(timeStamp);
        Log.e(TAG,"time is: "+hour);
        if (hour>0&&hour<12)
        {
            tvHeaderEmailUser.setText("בוקר טוב");
            //morning
        }else if (hour>=12&&hour<16)
        {
            tvHeaderEmailUser.setText("צהריים טובים");

            //afternoon
        }else if (hour >=16&&hour < 20)
        {
            //evening
            tvHeaderEmailUser.setText("ערב טוב");

        }
        else{
            //night
            tvHeaderEmailUser.setText("לילה טוב");

        }




    }

    private void authManager() {

        navView = (NavigationView) findViewById(R.id.nav_view);
        tvHeaderHelloUser = findViewById(R.id.tvHeader_hello_user);
        tvHeaderEmailUser = findViewById(R.id.tvHeader_email);
        mAuth = FirebaseAuth.getInstance();
        authDrawerInit();

        if (fbUser!=null)
        {
            mServerHandler.SetOnUserFetchedListener(new ServerHandler.OnUserFetchedListener() {
                @Override
                public void OnUserFetch(User i_user) {
                    if (i_user!=null)
                    {
                        if (i_user != null) {
                            //registered user
                            Log.e(TAG, "משתמש רשום");
                            signedUpUser = i_user;
                        } else {
                            Log.e(TAG, "משתמש נרשם");
                            //just singed up
                            if (signedUpUser != null) {
                                mServerHandler.writeUser(signedUpUser);
                            } else {
                                Log.e(TAG, "משתמש גוגל לא רשום");
                                //google and not written in database
                                signedUpUser = new User();
                                signedUpUser.setM_email(fbUser.getEmail());
                                mServerHandler.writeUser(signedUpUser);
                            }
                        }
                        initNavigatorHeader();
                    }

                }
            });
            mServerHandler.fetchUser(fbUser.getUid());
        }


    }

    private void authDrawerInit()
    {
        Menu nav_Menu = navView.getMenu();

        if (fbUser != null) {
            //authenticated user
            nav_Menu.findItem(R.id.nav_log_in).setVisible(false);
            mServerHandler.fetchUser(fbUser.getUid());
        } else {
            //not authenticated user

            nav_Menu.findItem(R.id.nav_log_out).setVisible(false);
            nav_Menu.findItem(R.id.nav_favorites).setVisible(false);
            nav_Menu.findItem(R.id.nav_profile).setVisible(false);
        }
    }

    private void drawerManager() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mSavedInstanceState == null) {
            HomeFrag homeFrag = new HomeFrag();
            homeFrag.initHomeFrag(getApplicationContext(), signedUpUser);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFrag).commit();
        }

        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            Log.e(TAG, "onActivityResultMain");

            mProfileFrag.onActivityResult(requestCode, resultCode, data);

        }
    }
}
