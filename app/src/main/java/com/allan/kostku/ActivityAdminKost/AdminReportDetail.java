package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.allan.kostku.Model.Report;
import com.allan.kostku.R;

import java.text.DateFormat;

public class AdminReportDetail extends AppCompatActivity {
    private Report report;
    private TextView tvReportTitle, tvReportDate, tvReportDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail_admin);
        report = (Report) getIntent().getExtras().getSerializable("REPORT_DATA");
        initToolbar();
        initReportData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report-Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initReportData(){
        //Set Report Title
        tvReportTitle = (TextView) findViewById(R.id.tvReportTitle);
        tvReportTitle.setText(report.getReportTitle());
        //Set Report Date
        tvReportDate = (TextView) findViewById(R.id.tvReportDate);
        tvReportDate.setText(DateFormat.getDateTimeInstance().format(report.getTimestampCreatedLong()));
        //Set Report Detail
        tvReportDetail = (TextView)findViewById(R.id.tvReportDetail);
        tvReportDetail.setText(report.getReportDesc());
    }
}
