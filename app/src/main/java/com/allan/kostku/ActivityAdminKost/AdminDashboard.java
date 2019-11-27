package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        initToolbar();
        initData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin-Dashboard");
        Log.e( "TESTTT: ",ResourceManager.LOGGED_USER.getUserEmail()+"" );
    }

    private void initData(){
        // List Room Activity
        CardView cvRoom = (CardView)findViewById(R.id.cvRoom);
        cvRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRoom = new Intent(AdminDashboard.this, AdminRoom.class);
                startActivity(intentRoom);
            }
        });
        // List Boarding Tenant (User) Activity
        CardView cvTenant = (CardView)findViewById(R.id.cvTenant);
        cvTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTenant = new Intent(AdminDashboard.this, AdminTenant.class);
                startActivity(intentTenant);
            }
        });
        //List Report Activity
        TextView tvReportTotal = (TextView)findViewById(R.id.tvReportTotal);
//        tvReportTotal.setText();
        CardView cvReport = (CardView)findViewById(R.id.cvReport);
        cvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReport = new Intent(AdminDashboard.this, AdminReport.class);
                startActivity(intentReport);
            }
        });

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
                ResourceManager.logOutUser(AdminDashboard.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

