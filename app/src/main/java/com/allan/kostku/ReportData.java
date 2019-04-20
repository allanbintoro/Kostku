package com.allan.kostku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.allan.kostku.model.Report;

public class ReportData extends AppCompatActivity {
    private Report report;
    TextView reportTitle, reportDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_data);
        Intent intent = getIntent();
        report = (Report) intent.getSerializableExtra("report_data");

        reportTitle = (TextView)findViewById(R.id.reportTitle);
        reportDesc = (TextView)findViewById(R.id.reportDesc);

        reportTitle.setText(report.getReportTitle());
        reportDesc.setText(report.getReportDesc());
    }
}