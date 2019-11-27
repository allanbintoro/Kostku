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

import com.allan.kostku.Adapter.AdminReportAdapter;
import com.allan.kostku.Model.Report;
import com.allan.kostku.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AdminReport extends AppCompatActivity {
    private static final String TAG = "AdminReport";
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    private RecyclerView rvReportList;
    private DatabaseReference reportRef;
    private ArrayList<Report> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        //Init Toolbar
        initToolbar();
        //Init Firebase Ref
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reportRef = FirebaseDatabase.getInstance().getReference("Report");
        //Init Recycler View
        initRcv();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report-List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvReportList = findViewById(R.id.rvReportList);
        rvReportList.setHasFixedSize(true);
        rvReportList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUserInformation();
    }

    private void loadUserInformation() {
        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportList = new ArrayList<>();
                reportList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    reportList.add(data.getValue(Report.class));
                }
                //Reverse report data
                Collections.reverse(reportList);
                AdminReportAdapter adminReportAdapter = new AdminReportAdapter(AdminReport.this, reportList);
                //Notify if the data has changed
                adminReportAdapter.notifyDataSetChanged();
                rvReportList.setAdapter(adminReportAdapter);
                adminReportAdapter.setOnItemClickListener(new AdminReportAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Report obj, int position) {
                        Intent intent = new Intent(AdminReport.this, AdminReportDetail.class);
                        intent.putExtra("REPORT_DATA", obj);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminReport.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
