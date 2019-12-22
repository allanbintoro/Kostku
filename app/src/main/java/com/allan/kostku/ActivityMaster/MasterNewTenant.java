package com.allan.kostku.ActivityMaster;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MasterNewTenant extends AppCompatActivity {
    private String userType, roomId, kostId;
    private Spinner spinnerKost, spinnerUserType, spinnerRoom;
    private EditText etUserName, etUserKtp, etUserEmail, etUserPassword1, etUserPassword2;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private CollectionReference userRef = db.collection("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_new_tenant);
        initToolbar();

        etUserName = (EditText) findViewById(R.id.etUserName);
        etUserKtp = (EditText) findViewById(R.id.etUserKtp);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        etUserPassword1 = (EditText) findViewById(R.id.etUserPassword1);
        etUserPassword2 = (EditText) findViewById(R.id.etUserPassword2);
        spinnerUserType = (Spinner) findViewById(R.id.spinnerUserType);
        final TextView textView3 = (TextView) findViewById(R.id.textView3);
        final TextView textView4 = (TextView) findViewById(R.id.textView4);
        spinnerKost = (Spinner) findViewById(R.id.spinnerKost);
        spinnerRoom = (Spinner) findViewById(R.id.spinnerRoom);

        ArrayList<String> kostList = ResourceManager.getKostNameById(ResourceManager.KOSTS, ResourceManager.LOGGED_USER.getUserId());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, kostList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set the layout resource to create the drop down views.
        spinnerKost.setAdapter(arrayAdapter); //Set the data to your spinner

        spinnerUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerUserType.getSelectedItem().toString().equals("Tenant")) {
                    textView3.setVisibility(View.VISIBLE);
                    spinnerKost.setVisibility(View.VISIBLE);
                } else {
                    textView3.setVisibility(View.GONE);
                    spinnerKost.setVisibility(View.GONE);
                    textView4.setVisibility(View.GONE);
                    spinnerRoom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerKost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!spinnerKost.getSelectedItem().toString().equals("")) {
                    textView4.setVisibility(View.VISIBLE);
                    spinnerRoom.setVisibility(View.VISIBLE);

                    String kostName = spinnerKost.getSelectedItem().toString();
                    kostId = ResourceManager.getKostIdByKostName(ResourceManager.KOSTS, kostName);
                    ArrayList<String> roomList = ResourceManager.getRoomByKostName(ResourceManager.ROOMS, kostId);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MasterNewTenant.this, android.R.layout.simple_spinner_item, roomList);
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
        String userRole = spinnerUserType.getSelectedItem().toString().trim();

        if (userRole.equals("Master")) {
            userType = "1";
            kostId = "";
            roomId = "";
        } else if (userRole.equals("Admin")) {
            userType = "2";
            kostId = "";
            roomId = "";
        } else if (userRole.equals("Tenant")) {
            String kostName = spinnerKost.getSelectedItem().toString().trim();
            String roomName = spinnerRoom.getSelectedItem().toString().trim();
            userType = "3";
            kostId = ResourceManager.getKostIdByKostName(ResourceManager.KOSTS, kostName);
            roomId = ResourceManager.getRoomByName(ResourceManager.ROOMS, kostId, roomName);
        }

        //checking if the value is provided
        if (userName.isEmpty()) {
            etUserName.requestFocus();
            return;
        } else if (userKTP.isEmpty()) {
            etUserKtp.requestFocus();
            return;
        } else if (userKTP.length() != 16) {
            Toast.makeText(this, "KTP Number Must 16 Digits", Toast.LENGTH_SHORT).show();
            etUserKtp.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            etUserEmail.requestFocus();
            return;
        } else if (userPassword1.isEmpty() || userPassword1.length() < 6) {
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            etUserPassword1.requestFocus();
            return;
        } else if (userPassword2.isEmpty()) {
            Toast.makeText(this, "Please Re Enter your password!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(MasterNewTenant.this, "Success Add new User", Toast.LENGTH_LONG).show();
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
