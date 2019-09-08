package com.mta.sadna19.sadna;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class resetPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView resetPasswordEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetPasswordEmail =findViewById(R.id.tvEmailToReset);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onResetPasswordClicked(View v){

        String email = resetPasswordEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(resetPasswordActivity.this, corridorActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                        }
                    }
                });
    }

}
