package com.allan.kostku;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = "ChangePassword";
    EditText etNewPass, etConfPass;
    Button updatePass;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();
        etNewPass = (EditText) findViewById(R.id.newPassword);
        etConfPass = (EditText) findViewById(R.id.confPassword);
        updatePass = (Button) findViewById(R.id.btn_update);
        update();


    }

    private void update() {
        updatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newPass = etNewPass.getText().toString().trim();
                final String confPass = etNewPass.getText().toString().trim();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (newPass.equalsIgnoreCase(confPass)) {
                        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ChangePassword.this, Me.class));
                                }
                            }
                        });
                    }
                }

            }
        });
    }
}
