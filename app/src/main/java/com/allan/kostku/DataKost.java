package com.allan.kostku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.allan.kostku.model.Kost;

public class DataKost extends AppCompatActivity {
    private EditText nama_kost;
    private TextView  umur_kost,fasilitas_kost;
    private Kost kost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kost);
        Intent intent = getIntent();
        kost = (Kost) intent.getSerializableExtra("KostAdapter_data");

        nama_kost = findViewById(R.id.tv_datakost_nama);
        umur_kost = findViewById(R.id.tv_datakost_umur);
        fasilitas_kost = findViewById(R.id.tv_datakost_fasilitas);

        nama_kost.setText(kost.getNama());
        umur_kost.setText(kost.getUmur());
        fasilitas_kost.setText(kost.getFasilitas());
    }
}
