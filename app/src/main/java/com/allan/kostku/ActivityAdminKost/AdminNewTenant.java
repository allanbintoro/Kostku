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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.allan.kostku.ActivityMaster.MasterNewTenant;
import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
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
    private String  userType, roomId, kostId;
    private Spinner spinnerKost,  spinnerRoom;
    private EditText etUserName, etUserKtp, etUserEmail, etUserPassword1, etUserPassword2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private CollectionReference userRef = db.collection("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_tenant);
        initToolbar();
        etUserName = (EditText) findViewById(R.id.etUserFullName);
        etUserKtp = (EditText) findViewById(R.id.etUserKtp);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        etUserPassword1 = (EditText) findViewById(R.id.etUserPassword1);
        etUserPassword2 = (EditText) findViewById(R.id.etUserPassword2);
        spinnerKost = (Spinner) findViewById(R.id.spinnerKost);
        spinnerRoom = (Spinner) findViewById(R.id.spinnerRoom);

        ArrayList<String> kostList = ResourceManager.getKostName(ResourceManager.KOSTS);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kostList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set the layout resource to create the drop down views.
        spinnerKost.setAdapter(arrayAdapter); //Set the data to your spinner

        spinnerKost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spinnerKost.getSelectedItem().toString().equals("")) {
                    String kostName = spinnerKost.getSelectedItem().toString();
                    kostId = ResourceManager.getKostIdByKostName(ResourceManager.KOSTS, kostName);
                    ArrayList<String> roomList = ResourceManager.getRoomByKostName(ResourceManager.ROOMS, kostId);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminNewTenant.this, android.R.layout.simple_spinner_item, roomList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set the layout resource to create the drop down views.
                    spinnerRoom.setAdapter(adapter); //Set the data to your spinner
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        //getting the values to save
        final String userName = etUserName.getText().toString().trim();
        final String userKTP = etUserKtp.getText().toString().trim();
        final String userEmail = etUserEmail.getText().toString().trim();
        String userPassword1 = etUserPassword1.getText().toString().trim();
        String userPassword2 = etUserPassword2.getText().toString().trim();

        String kostName = spinnerKost.getSelectedItem().toString().trim();
        String roomName = spinnerRoom.getSelectedItem().toString().trim();
        userType = "3";
        kostId = ResourceManager.getKostIdByKostName(ResourceManager.KOSTS, kostName);
        roomId = ResourceManager.getRoomByName(ResourceManager.ROOMS, kostId, roomName);

        //checking if the value is provided
        if (userName.isEmpty()) {
            etUserName.requestFocus();
            Toast.makeText(this, "Name Can't Empty", Toast.LENGTH_SHORT).show();
        }  else if (userKTP.length() < 16) {
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
            createNewUser(userName, userKTP, userEmail, userPassword1, userType, roomId, kostId)
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            Toast.makeText(AdminNewTenant.this, "Success Add new User", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        }
    }

    public static Task<String> createNewUser(String userFullName, String userKtp,
                                             String userEmail, String userPassword,
                                             String userType, String roomId, String kostId) {
        User user = new User();
        Gson gson = new Gson();

        user.setUserName(userFullName);
        user.setUserKtp(userKtp);
        user.setUserEmail(userEmail);
        user.setUserPassword(userPassword);
        user.setUserType(userType);
        user.setKostId(kostId);
        user.setRoomId(roomId);


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
                        Log.e("AKAKAKK", result + "");
                        return result;
                    }
                });
    }
}
