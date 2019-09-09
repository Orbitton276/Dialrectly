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
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpDialog extends AppCompatDialogFragment {
    public static final String TAG = "SignUpDialog";
    private FirebaseAuth mAuth;
    private EditText mEmail, mDisplayName;
    private EditText mID, mPhone;
    private EditText mPass, btnReEnterPassword;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnSignUp;
    private ServerHandler mServerHandler;

    public SignUpDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.signup_dialog, null);
        builder.setView(view);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEmailPasswordSignUpClick();
            }
        });
        mEmail = view.findViewById(R.id.etEmail);
        mPass = view.findViewById(R.id.etPassword);
        mID = view.findViewById(R.id.etID);
        mPhone = view.findViewById(R.id.etPhone);
        btnReEnterPassword = view.findViewById(R.id.etReEnterPassword);
        mDisplayName = view.findViewById(R.id.etDisplayName);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthenticationInit();
        return builder.create();
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



    private void registerUser()

    {
        User newUserToRegister = new User();
        newUserToRegister.setM_email(mEmail.getText().toString());
        newUserToRegister.setM_name(mDisplayName.getText().toString());
        mServerHandler = new ServerHandler();
        mServerHandler.writeUser(newUserToRegister);

    }

    public void displayMessage(String message) {
        if (message != null) {
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private boolean verifyPassword(String i_password, String i_reEnteredPassword) {


        if (mDisplayName.getText().length() < 3) {
            displayMessage("Display name must be at least 3 characters");
            return false;
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

    public void onEmailPasswordSignUpClick() {

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


            authResult.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

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
                                            registerUser();
                                        }
                                    }
                                });
                        displayMessage("successfully signed up");

                        Intent intent = new Intent(getActivity(), MenuListActivity.class);
                        startActivity(intent);

                    } else {
                        displayMessage("Problem signing up.\ndetails: " + task.getException().getMessage());
                    }


                    Log.e(TAG, "Email/Pass Auth: onComplete() <<");
                }
            });
        }


        Log.e(TAG, "onEmailPasswordSignUpClick() <<");
    }

}