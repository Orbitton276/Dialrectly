/*package com.mta.sadna19.sadna;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class signinActivity extends AppCompatActivity {

    public static final String TAG = "onSignInActivity";
    public static final int RC_GOOGLE_SIGN_IN = 1001;
    private SignInButton mGoogleSignInBtn;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mConfig;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //private AccessTokenTracker accessTokenTracker;
    private TextView textView,mEmail,mPass;

    //private AnalyticsManager analyticsManager = AnalyticsManager.getInstance();
    private String signInMethod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //setContentView(R.layout.signin_dialog);

        //AnalyticsManager.getInstance().init(getApplicationContext());

        initSignInActivity();
        firebaseInit();
        googleSignInInit();
        //openDialog();

        //initRemoteConfig();
    }


    //------------------------------------------------------------------------------------

    private void initSignInActivity(){
        mGoogleSignInBtn = (SignInButton)findViewById(R.id.google_sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        //mConfig = FirebaseRemoteConfig.getInstance();
        mEmail = findViewById(R.id.tvSigninEmail);
        mPass = findViewById(R.id.tvSigninPassword);
    }



    public void onEmailPasswordAuthClick(View V) {

        Log.e(TAG, "onEmailPasswordSignUpClick() >>");

        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (email.isEmpty()||pass.isEmpty()){
            //displayMessage("Please fill in both Email and Password fields");
            return;
        }

        Task<AuthResult> authResult;

        authResult = mAuth.signInWithEmailAndPassword(email, pass);
        authResult.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.e(TAG, "Email/Pass Auth: onComplete() >> " + task.isSuccessful());
                if (task.isSuccessful()){
                    //displayMessage("successfully signed in");


                    Intent intent;
                    intent = new Intent(signinActivity.this, MenuListActivity.class);
                    startActivity(intent);
                    finish();


                }

                Log.e(TAG, "Email/Pass Auth: onComplete() <<");
            }
        });

        Log.e(TAG, "onEmailPasswordSignUpClick() <<");
    }

    private void googleSignInInit() {

        Log.e(TAG, "googleSigninInit() >>" );

        // Configure Google Sign In
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoogleSignIn();
            }
        });
        googleButtonInit();
        Log.e(TAG, "googleSigninInit() <<" );
    }

    private void googleButtonInit(){
        textView = (TextView) mGoogleSignInBtn.getChildAt(0);
        textView.setText(getResources().getString(R.string.googleButtonText));
        textView.setTextSize(20);
        textView.setPadding(40,0,0,0);
    }

    private void onGoogleSignIn() {

        Log.e(TAG, "onGoogleSignIn() >>" );

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);

        Log.e(TAG, "onGoogleSignIn() <<" );
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.e(TAG, "firebaseAuthWithGoogle() >>");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(signinActivity.this, MenuListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

        Log.e(TAG, "firebaseAuthWithGoogle() <<");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Log.e(TAG, "onActivityResult () >>");

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            //Google Login...
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.e(TAG, "try google sign in success () >>");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google sign in failed", e);
            }
        }
        Log.e(TAG, "onActivityResult () <<");
    }

    @Override
    protected void onStart() {

        Log.e(TAG, "onStart() >>");
        String name;
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){

            Log.e(TAG,"user already signed up"+user.getUid());
            Log.e(TAG,"user already signed up"+user.getEmail());
            Intent intent = new Intent(signinActivity.this,MenuListActivity.class);
            startActivity(intent);
            finish();
        }

        Log.e(TAG, "onStart() <<");

    }

    @Override
    protected void onStop() {

        Log.e(TAG, "onStop() >>");

        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        Log.e(TAG, "onStop() <<");

    }


    private void firebaseInit() {
        Log.e(TAG, "firebaseAuthenticationInit() >>");
        //Obtain reference to the current authentication
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.e(TAG, "onAuthStateChanged() >>");

                Log.e(TAG, "onAuthStateChanged() <<");
            }
        };

        Log.e(TAG, "firebaseAuthenticationInit() <<");
    }

    public void onToSignUpClicked(View v)
    {
        Intent intent = new Intent(signinActivity.this, signupActivity.class);
        startActivity(intent);
    }

    public void onForgotPasswordClicked(View v){
        Intent intent = new Intent(signinActivity.this,resetPasswordActivity.class);
        startActivity(intent);
    }

    public void onContinueToServicesClicked(View v)
    {
        Intent intent = new Intent(signinActivity.this,MenuListActivity.class);
        startActivity(intent);
    }



}*/



