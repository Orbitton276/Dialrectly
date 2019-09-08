package com.mta.sadna19.sadna;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class SignInDialog extends AppCompatDialogFragment {
    public static final String TAG = "onSignInDialog";
    private Button btnSignInEmailPass;
    private TextView tvResetPassword;
    private SignInButton mGoogleSignInBtn;
    public static final int RC_GOOGLE_SIGN_IN = 1001;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mConfig;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //private AccessTokenTracker accessTokenTracker;
    private TextView textView,mEmail,mPass;

    //private AnalyticsManager analyticsManager = AnalyticsManager.getInstance();
    private String signInMethod = null;
    public SignInDialog(){
        //googleButtonInit();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.signin_dialog,null);
        builder.setView(view);
        tvResetPassword = view.findViewById(R.id.tvResetPassword);
        btnSignInEmailPass = view.findViewById(R.id.bSignIn);
        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onForgotPasswordClicked();
            }
        });
        btnSignInEmailPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailPasswordAuthClick();
            }
        });

        mGoogleSignInBtn = (SignInButton)view.findViewById(R.id.google_sign_in_button);
        googleButtonInit();
        //initSignInActivity();
        firebaseInit();
        googleSignInInit();
        mEmail = view.findViewById(R.id.tvSigninEmail);
        mPass = view.findViewById(R.id.tvSigninPassword);


        return builder.create();
    }


    private void googleButtonInit(){
        textView = (TextView) mGoogleSignInBtn.getChildAt(0);
        textView.setText(getResources().getString(R.string.googleButtonText));
        textView.setTextSize(20);
        textView.setPadding(40,0,0,0);
    }



    private void initSignInActivity(){
        //mGoogleSignInBtn = (SignInButton)getView().findViewById(R.id.google_sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        //mConfig = FirebaseRemoteConfig.getInstance();
        mEmail = getView().findViewById(R.id.tvSigninEmail);
        mPass = getView().findViewById(R.id.tvSigninPassword);
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

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        mGoogleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoogleSignIn();
            }
        });
        googleButtonInit();
        Log.e(TAG, "googleSigninInit() <<" );
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(getActivity(), MenuListActivity.class);
                            startActivity(intent);
                            //finish();

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


    public void onEmailPasswordAuthClick() {

        Log.e(TAG, "onEmailPasswordSignUpClick() >>");

        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();

        if (email.isEmpty()||pass.isEmpty()){
            //displayMessage("Please fill in both Email and Password fields");
            return;
        }

        Task<AuthResult> authResult;

        authResult = mAuth.signInWithEmailAndPassword(email, pass);
        authResult.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.e(TAG, "Email/Pass Auth: onComplete() >> " + task.isSuccessful());
                if (task.isSuccessful()){
                    Intent intent = new Intent(getActivity(), MenuListActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "בעיה בהתחברות. אנא בדוק את הפרטים", Toast.LENGTH_SHORT).show();
                }

                Log.e(TAG, "Email/Pass Auth: onComplete() <<");
            }
        });

        Log.e(TAG, "onEmailPasswordSignUpClick() <<");
    }
    public void onForgotPasswordClicked(){
        Intent intent = new Intent(getActivity(),resetPasswordActivity.class);
        startActivity(intent);
    }
}
