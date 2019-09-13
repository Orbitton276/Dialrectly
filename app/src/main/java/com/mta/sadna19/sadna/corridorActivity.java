package com.mta.sadna19.sadna;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class corridorActivity extends AppCompatActivity {
    public static final int RC_GOOGLE_SIGN_IN = 1001;
    private SignInButton mGoogleSignInBtn;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mConfig;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textView,mEmail,mPass;
    private Button toSignIn,toSignUp,toServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corridor);
        toSignIn = findViewById(R.id.btnToSignIn);
        toSignUp = findViewById(R.id.btnToSignUp);
        toServices = findViewById(R.id.btnToServices);

        toSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignInDialog();
            }
        });
        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpDialog();
            }
        });

        toServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onContinueToServicesClicked(v);
            }
        });

        firebaseInit();
    }

    private void openSignInDialog()
    {
        SignInDialog dialog = new SignInDialog();
        dialog.show(getSupportFragmentManager(),"");
    }
    private void openSignUpDialog()
    {

        SignUpDialog dialog = new SignUpDialog();
        dialog.show(getSupportFragmentManager(),"");
    }

    private void initSignInActivity(){
        mGoogleSignInBtn = (SignInButton)findViewById(R.id.google_sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        //mConfig = FirebaseRemoteConfig.getInstance();
        mEmail = findViewById(R.id.tvSigninEmail);
        mPass = findViewById(R.id.tvSigninPassword);
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Intent intent = new Intent(corridorActivity.this, MenuListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });


    }


    @Override
    protected void onStart() {

        String name;
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){


            Intent intent = new Intent(corridorActivity.this,MenuListActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onStop() {


        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }


    private void firebaseInit() {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

    }


    public void onContinueToServicesClicked(View v)
    {
        Intent intent = new Intent(corridorActivity.this,MenuListActivity.class);
        startActivity(intent);
    }

}
