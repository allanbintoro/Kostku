package com.allan.kostku.ActivityMaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.allan.kostku.ActivityAdminKost.AdminDashboard;
import com.allan.kostku.ActivityAdminKost.AdminReport;
import com.allan.kostku.ActivityAdminKost.AdminRoom;
import com.allan.kostku.ActivityAdminKost.AdminTenant;
import com.allan.kostku.ActivityUser.MainActivity;
import com.allan.kostku.Model.User;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;

import java.util.ArrayList;

public class MasterDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_dashboard);
        initToolbar();
        initData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Master-Dashboard");
    }

    private void initData(){
        // List Kost Activity
        CardView cvKost = (CardView)findViewById(R.id.cvKost);
        cvKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRoom = new Intent(MasterDashboard.this, MasterKost.class);
                startActivity(intentRoom);
            }
        });



        // List Boarding Tenant (User) Activity
        CardView cvTenant = (CardView)findViewById(R.id.cvTenant);
        cvTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTenant = new Intent(MasterDashboard.this, MasterTenantList.class);
                startActivity(intentTenant);
            }
        });
        //List Report Activity
//        TextView tvReportTotal = (TextView)findViewById(R.id.tvReportTotal);
////        tvReportTotal.setText();
//        CardView cvReport = (CardView)findViewById(R.id.cvReport);
//        cvReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentReport = new Intent(MasterDashboard.this, AdminReport.class);
//                startActivity(intentReport);
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_logout:
                ResourceManager.logOutUser(MasterDashboard.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

