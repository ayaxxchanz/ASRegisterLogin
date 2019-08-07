package com.example.asus.project5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    ProgressBar mprogressBar;
    EditText email;
    TextView back;
    Button resetbtn;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        email = (EditText) findViewById(R.id.email_pass_forgot);
        back = (TextView) findViewById(R.id.textForgot);
        resetbtn = (Button) findViewById(R.id.forgotbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        MainActivity.class));
            }
        });

        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                ResetPass();
            }
        });

    }
    public void ResetPass(){
        String Email = email.getText().toString().trim();

        if (TextUtils.isEmpty(Email)){
            mprogressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill in the field.", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mprogressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassActivity.this,"Password sent.",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),
                            MainActivity.class));
                }
                else{
                    Toast.makeText(ForgotPassActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
