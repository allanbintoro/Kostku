package com.allan.kostku;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.allan.kostku.model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportDialog extends AppCompatActivity {
    private DatabaseReference database;
    FirebaseAuth firebaseAuth;

    private Spinner spinnerReportTitle;
    private EditText et_desc;
    private Button btn_Lapor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_report);
        setTitle("Report Dialog");

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("Report");

        spinnerReportTitle = findViewById(R.id.spinnerReportTitle);
        et_desc = findViewById(R.id.et_desc);
        btn_Lapor = findViewById(R.id.btn_Lapor);

        btn_Lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReport();
            }
        });
    }

    private void addReport(){
        String reportTitle = spinnerReportTitle.getSelectedItem().toString();
        String reportDesc = et_desc.getText().toString();
        if(reportDesc.equals("")){
            et_desc.setError("Silahkan masukkan keluhan");
            et_desc.requestFocus();
        }else{
            String id = database.push().getKey();
            String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            Report report = new Report(id, reportTitle, reportDesc, user);
            database.child(id).setValue(report);
            Toast.makeText(this, "Report Submitted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ReportDialog.this,ListReport.class));
        }
    }

}
