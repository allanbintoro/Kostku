package com.allan.kostku.ActivityMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Adapter.KostAdapter;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MasterKost extends AppCompatActivity {
    private RecyclerView rvKostList;
    private KostAdapter kostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_kost);

        initToolbar();
        initRcv();
        FloatingActionButton fabAddKost = (FloatingActionButton) findViewById(R.id.fabAddKost);
        fabAddKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MasterKost.this, MasterNewKost.class));
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kost-List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvKostList = findViewById(R.id.rvKostList);

        rvKostList.setHasFixedSize(true);
        rvKostList.setLayoutManager(new LinearLayoutManager(this));
        kostAdapter = new KostAdapter(MasterKost.this, ResourceManager.KOSTS);
        rvKostList.setAdapter(kostAdapter);

        kostAdapter.refreshItem(ResourceManager.KOSTS);
        kostAdapter.notifyDataSetChanged();

        kostAdapter.setOnItemClickListener(new KostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Kost obj, int position) {
                Intent intent = new Intent(MasterKost.this, MasterKostDetail.class);
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
