package com.allan.kostku.ActivityAdminKost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Adapter.UserAdapter;
import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AdminTenant extends AppCompatActivity {
    private RecyclerView rvUserList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_tenant_list);
        initToolbar();
        initRcv();
        FloatingActionButton fabAddUser = (FloatingActionButton) findViewById(R.id.fabAddUser);
        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminTenant.this, AdminNewTenant.class));
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User-List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvUserList = findViewById(R.id.rvUserList);

        rvUserList.setHasFixedSize(true);
        rvUserList.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> kostLists = ResourceManager.getKostList(ResourceManager.KOSTS, ResourceManager.LOGGED_USER.getUserId());
        ArrayList<String> userUidList = ResourceManager.getUsersListByUid(ResourceManager.ROOMS, kostLists);
        ArrayList<User> userList = ResourceManager.getUserByUIDRoom(ResourceManager.USERS, userUidList);
        userAdapter = new UserAdapter(AdminTenant.this, userList);
        rvUserList.setAdapter(userAdapter);

        userAdapter.refreshItem(userList);
        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User obj, int position) {
//                Intent intent = new Intent(MasterTenantList.this, Test.class);
//                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> kostLists = ResourceManager.getKostList(ResourceManager.KOSTS, ResourceManager.LOGGED_USER.getUserId());
        ArrayList<String> userUidList = ResourceManager.getUsersListByUid(ResourceManager.ROOMS, kostLists);
        ArrayList<User> userList = ResourceManager.getUserByUIDRoom(ResourceManager.USERS, userUidList);
        userAdapter.refreshItem(userList);
    }
}
