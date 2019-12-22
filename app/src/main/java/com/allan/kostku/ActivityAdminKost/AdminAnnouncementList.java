package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.allan.kostku.Adapter.KostAdapter;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminAnnouncementList extends AppCompatActivity {

    private RecyclerView rvAnnouncementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcement_list);
        initToolbar();
        initRcv();
        FloatingActionButton fabAddAnnouncement = (FloatingActionButton) findViewById(R.id.fabAddAnnouncement);
        fabAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAnnouncementList.this, AdminNewAnnouncement.class));
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Announcement - List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvAnnouncementList = findViewById(R.id.rvAnnouncementList);
        rvAnnouncementList.setHasFixedSize(true);
        rvAnnouncementList.setLayoutManager(new LinearLayoutManager(this));
//        kostAdapter = new KostAdapter(AdminKost.this, ResourceManager.KOSTSUID);
//        rvAnnouncementList.setAdapter(kostAdapter);

//        kostAdapter.setOnItemClickListener(new KostAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, Kost obj, int position) {
//                Intent intent = new Intent(AdminKost.this, AdminKostDetail.class);
//                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        kostAdapter.refreshItem(ResourceManager.KOSTSUID);
    }
}
