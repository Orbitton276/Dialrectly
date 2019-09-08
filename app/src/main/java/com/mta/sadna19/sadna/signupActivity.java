/*package com.mta.sadna19.sadna;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signupActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private EditText mEmail, mDisplayName;
    private EditText mID, mPhone;
    private EditText mPass, btnReEnterPassword;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User userr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mEmail = findViewById(R.id.etEmail);
        mPass = findViewById(R.id.etPassword);
        mID = findViewById(R.id.etID);
        mPhone = findViewById(R.id.etPhone);
        btnReEnterPassword = findViewById(R.id.etReEnterPassword);
        mDisplayName = findViewById(R.id.etDisplayName);
        mAuth = FirebaseAuth.getInstance();
        userr = new User();
        firebaseAuthenticationInit();
    }

    private void firebaseAuthenticationInit() {

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

    @Override
    protected void onStart() {

        Log.e(TAG, "onStart() >>");

        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

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


    public void onEmailPasswordSignUpClick(View V) {

        Log.e(TAG, "onEmailPasswordSignUpClick() >>");

        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();
        String reEnterPassword = btnReEnterPassword.getText().toString();


        if (email.isEmpty()) {
            displayMessage("Please fill in all fields");
            return;
        }
        if (verifyPassword(pass, reEnterPassword)) {
            Task<AuthResult> authResult;
            authResult = mAuth.createUserWithEmailAndPassword(email, pass);


            authResult.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    Log.e(TAG, "Email/Pass Auth: onComplete() >> " + task.isSuccessful());
                    FirebaseUser user = mAuth.getCurrentUser();

                    if (task.isSuccessful()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mDisplayName.getText().toString())
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });
                        updateNewUser();
                        displayMessage("successfully signed up");

                        Intent intent = new Intent(signupActivity.this, MenuListActivity.class);
                        intent.putExtra("user",userr);
                        startActivity(intent);
                        finish();
                    } else {
                        displayMessage("Problem signing up.\ndetails: " + task.getException().getMessage());
                    }


                    Log.e(TAG, "Email/Pass Auth: onComplete() <<");
                }
            });
        }


        Log.e(TAG, "onEmailPasswordSignUpClick() <<");
    }


    public void displayMessage(String message) {
        if (message != null) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private boolean verifyPassword(String i_password, String i_reEnteredPassword) {

        if (mDisplayName.getText().length() < 3) {
            displayMessage("Display name must be at least 3 characters");
        }
        if (i_reEnteredPassword.equals(i_password)) {
            if (!checkPassword(i_password)) {
                displayMessage(getString(R.string.errorPasswordPattern));
                return false;
            }
        } else {
            displayMessage(getString(R.string.errorReEnteredPasswordPattern));
            return false;
        }


        return true;

    }

    private boolean checkPassword(String i_password) {

        String regExpn = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

        if (i_password == null) return false;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(i_password);

        return (matcher.matches());
    }

    private void updateNewUser() {


        if (mPhone != null) {
            userr.setM_phone(mPhone.getText().toString());
        }
        if (mDisplayName != null) {
            userr.setM_name(mDisplayName.getText().toString());
        }
        if (mID != null) {
            userr.setM_ID(mID.getText().toString());
        }
        if (mEmail != null) {
            userr.setM_email(mEmail.getText().toString());
        }

    }

}*/
