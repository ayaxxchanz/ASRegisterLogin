package com.example.asus.project5;

import android.content.Intent;
import android.os.Handler;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText password, email, cpassword, username;
    private Button button_register;
    private TextView signin;
    boolean doubleTap = false;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username =(EditText) findViewById(R.id.signup_username);
        email = (EditText) findViewById(R.id.signup_email_input);
        password =(EditText) findViewById(R.id.signup_password_input);
        cpassword =(EditText) findViewById(R.id.signup_cpass);

        button_register = (Button)findViewById(R.id.button_register);
        mAuth = FirebaseAuth.getInstance();

        signin = (TextView) findViewById(R.id.textSignIn);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == signin){
                    startActivity(new Intent(getApplicationContext(),
                            MainActivity.class));
                }
            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_register){
                    mProgressBar.setVisibility(View.VISIBLE);
                    createAccount();
                }
            }
        });
    }
    public void  createAccount(){
        final String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Username = username.getText().toString().trim();
        String CPassword = cpassword.getText().toString().trim();
        //Check if the field are not empty
        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Username) || TextUtils.isEmpty(CPassword)){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Check if Password = Confirm Password
        if (!Password.equals(CPassword)){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Password does not match.", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            //check if successful
                            mProgressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                //User is successfully registered and logged in
                                User user = new User(Username,Email,Password);
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mProgressBar.setVisibility(View.GONE);
                                        if(task.isSuccessful()){
                                            //start Profile Activity here
                                            Toast.makeText(RegisterActivity.this, "Registration successful.",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                                        }
                                        else{
                                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        if (doubleTap) {
            super.onBackPressed();
            finishAffinity();
        }

        else{
            Toast.makeText(this,"Press again to exit.",Toast.LENGTH_SHORT).show();
            doubleTap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap = false;
                }
            }, 2000);
        }
    }
}