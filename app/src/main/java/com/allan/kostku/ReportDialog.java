package com.allan.kostku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.allan.kostku.model.Report;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportDialog extends AppCompatActivity {
    private DatabaseReference database;

    private EditText etJudul, etDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_report);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance().getReference();
        etJudul = findViewById(R.id.et_Judul);
        etDeskripsi = findViewById(R.id.et_Deksripsi);

        findViewById(R.id.btn_Lapor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sJudul = etJudul.getText().toString();
                String sDesk = etDeskripsi.getText().toString();
                if(sJudul.equals("")){
                    etJudul.setError("Silahkan masukkan Judul");
                    etJudul.requestFocus();
                }else if(sDesk.equals("")){
                    etDeskripsi.setError("Silahkan masukkan keluhan");
                    etDeskripsi.requestFocus();
                }else{
                    submitReport(new Report(sJudul.toLowerCase(), sDesk.toLowerCase()));

                }
            }
        });



    }

    private void submitReport(Report report) {
        database.child("Report")
                .push()
                .setValue(report)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                etJudul.setText("");
                etDeskripsi.setText("");

                Toast.makeText(ReportDialog.this, "Report telah di submit", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ReportDialog.this,ListReport.class));

            }
        });
    }
}
