package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allan.kostku.Adapter.KostAdapter;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;

public class AdminRoom extends AppCompatActivity {

    private RecyclerView rvRoomList;
    KostAdapter kostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_room);
        initToolbar();
        initRcv();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Room-List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvRoomList = findViewById(R.id.rvRoomList);
        rvRoomList.setHasFixedSize(true);
        rvRoomList.setLayoutManager(new LinearLayoutManager(this));
        kostAdapter = new KostAdapter(AdminRoom.this, ResourceManager.KOSTSUID);
        rvRoomList.setAdapter(kostAdapter);

        kostAdapter.setOnItemClickListener(new KostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Kost obj, int position) {
                Intent intent = new Intent(AdminRoom.this, AdminDashboard.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
