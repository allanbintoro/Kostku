package com.allan.kostku.ActivityMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.allan.kostku.Adapter.UserAdapter;
import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;

public class MasterTenantList extends AppCompatActivity {
    private RecyclerView rvUserList;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_tenant_list);
        initToolbar();
        initRcv();
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
        userAdapter = new UserAdapter(MasterTenantList.this, ResourceManager.USERS);
        rvUserList.setAdapter(userAdapter);

        userAdapter.refreshItem(ResourceManager.USERS);
        userAdapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, User obj, int position) {
                Intent intent = new Intent(MasterTenantList.this, MasterKostDetail.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                startActivity(intent);
            }
        });
    }
}
