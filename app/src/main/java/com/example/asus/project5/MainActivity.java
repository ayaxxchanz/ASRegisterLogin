package com.example.asus.project5;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Button button;
    private TextView register;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser!= null) {
            startActivity(new Intent(getApplicationContext(),
                    ProfileActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (EditText)findViewById(R.id.login_email_input);
        password = (EditText)findViewById(R.id.login_password_input);

        button = (Button)findViewById(R.id.login);
        register = (TextView) findViewById(R.id.textReg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == register){
                    startActivity(new Intent(getApplicationContext(),
                            RegisterActivity.class));
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button){
                    LoginUser();
                }
            }
        });
    }
    public void LoginUser(){
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if (TextUtils.isEmpty(Email)){
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(),
                                    ProfileActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this, "Failed to sign in. Email/password does not match.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}