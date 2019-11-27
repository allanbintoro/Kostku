package com.allan.kostku.ActivityAdminKost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.allan.kostku.Adapter.AdminTenantAdapter;
import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminTenant extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    private ArrayList<User> userList;
    RecyclerView rvUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tenant);
        initToolbar();
        //Init Firebase Ref
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User-List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        // Init Recycler View
        rvUserList = findViewById(R.id.rvUserList);
        rvUserList.setHasFixedSize(true);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAdd = (FloatingActionButton)findViewById(R.id.fabAddUser);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminTenant.this, AdminNewTenant.class));
            }
        });
    }

    private void loadUserInformation() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                userList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    userList.add(data.getValue(User.class));
                }
                AdminTenantAdapter adminTenantAdapter = new AdminTenantAdapter(AdminTenant.this, userList);
                //Notify if the data has changed
                adminTenantAdapter.notifyDataSetChanged();
                rvUserList.setAdapter(adminTenantAdapter);
                adminTenantAdapter.setOnItemClickListener(new AdminTenantAdapter.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, User obj, int position) {
                        Intent intent = new Intent(AdminTenant.this, AdminReportDetail.class);
//                        intent.putExtra("REPORT_DATA", obj);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminTenant.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserInformation();
    }
}
