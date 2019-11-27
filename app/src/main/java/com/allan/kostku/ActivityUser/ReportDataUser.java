package com.allan.kostku.ActivityUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.TextView;

import com.allan.kostku.Model.Report;
import com.allan.kostku.R;

public class ReportDataUser extends AppCompatActivity {
    private Report report;
    TextView reportTitle, reportDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_data);
        Intent intent = getIntent();
        report = (Report) intent.getSerializableExtra("REPORT_DATA");

        reportTitle = (TextView)findViewById(R.id.reportTitle);
        reportDesc = (TextView)findViewById(R.id.reportDesc);

        reportTitle.setText(report.getReportTitle());
        reportDesc.setText(report.getReportDesc());
    }


}
