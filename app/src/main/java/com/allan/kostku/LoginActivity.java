package com.allan.kostku;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allan.kostku.Activity.AdminDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    // defining views
    private Button mLogin, mRegister;
    // firebase auth object
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.getEmail().equals("allan@gmail.com")) {
                        Intent intent = new Intent(LoginActivity.this, AdminDashboard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };


        // initialize view
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);
        progressBar = (ProgressBar)findViewById(R.id.progressBarLogin);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            // method for user login
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                if (!password.isEmpty() && !email.isEmpty()) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    if (TextUtils.isEmpty(email) && password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please Insert Email and Password", Toast.LENGTH_SHORT).show();
                    } else if(email.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please Insert Email", Toast.LENGTH_SHORT).show();
                        mEmail.requestFocus();
                    } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Toast.makeText(LoginActivity.this, "Please Enter A Valid Email", Toast.LENGTH_SHORT).show();
                        mEmail.requestFocus();
                    } else if (password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please Insert Password", Toast.LENGTH_SHORT).show();
                        mPassword.requestFocus();
                    }
                }
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
