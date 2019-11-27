package com.allan.kostku.ActivityAdminKost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminNewTenant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_tenant);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Tenant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btnAddUser = (Button) findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTenant();
            }
        });
    }

    private void addNewTenant() {
        EditText etUserFullName = (EditText) findViewById(R.id.etUserFullName);
        EditText etUserKtp = (EditText) findViewById(R.id.etUserKtp);
        EditText etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        EditText etUserPassword1 = (EditText) findViewById(R.id.etUserPassword1);
        EditText etUserPassword2 = (EditText) findViewById(R.id.etUserPassword2);

        //getting the values to save
        final String userFullname = etUserFullName.getText().toString().trim();
        final String userKTP = etUserKtp.getText().toString().trim();
        final String userEmail = etUserEmail.getText().toString().trim();
        String userPassword1 = etUserPassword1.getText().toString().trim();
        String userPassword2 = etUserPassword2.getText().toString().trim();

        //checking if the value is provided
        if (userFullname.isEmpty()) {
            etUserFullName.requestFocus();
            return;
        } else if (userKTP.isEmpty()) {
            etUserKtp.requestFocus();
            return;
        } else if (userKTP.length() < 16) {
            Toast.makeText(this, "KTP Number not Complete", Toast.LENGTH_SHORT).show();
            etUserKtp.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            etUserEmail.requestFocus();
            return;
        } else if (userPassword1.isEmpty()) {
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            etUserPassword1.requestFocus();
            return;
        } else if (userPassword2.isEmpty()) {
            Toast.makeText(this, "Please enter cofirm password!", Toast.LENGTH_SHORT).show();
            etUserPassword2.requestFocus();
            return;
        } else if (!userPassword1.equals(userPassword2)) {
            Toast.makeText(this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
            etUserPassword1.requestFocus();
        } else {
            createNewUser(userFullname, userKTP, userEmail, userPassword1).addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminNewTenant.this, "Success Add new User", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AdminNewTenant.this, AdminTenant.class));
                    }
                }
            });
        }
    }

    public static Task<String> createNewUser(String userFullName, String userKtp,
                                             String userEmail, String userPassword) {
        User user = new User();
        Gson gson = new Gson();

        user.setUserName(userFullName);
        user.setUserKtp(userKtp);
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        user.setUserType("3");

        String param = gson.toJson(user);
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("jsonParams", param);

        return FirebaseFunctions.getInstance()
                .getHttpsCallable("createNewUser")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        Log.e("result", result + "");
                        return result;
                    }
                });
    }
}
