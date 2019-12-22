package com.allan.kostku.ActivityAdminKost;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.Model.Kost;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminNewAnnouncement extends AppCompatActivity {
    private Kost kost;
    private String kostId, announcementId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_announcement);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Announcement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        kost = (Kost) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        kostId = kost.getKostId();
        announcementId = kostRef.document().getId();
        Button btnAddAnnouncement = (Button) findViewById(R.id.btnAddAnnouncement);
        btnAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAnnouncement();
            }
        });
    }

    private void addNewAnnouncement() {
        EditText etAnnouncementTitle = (EditText) findViewById(R.id.etAnnouncementTitle);
        EditText etAnnouncementDesc = (EditText) findViewById(R.id.etAnnouncementDesc);


        //getting the values to save
        String announcementTitle = etAnnouncementTitle.getText().toString().trim();
        String announcementDesc = etAnnouncementDesc.getText().toString().trim();
        String announcementId = kostRef.document().getId();
        if (announcementTitle.isEmpty()) {
            etAnnouncementTitle.requestFocus();
            Toast.makeText(this, "Please Enter Announcement Title", Toast.LENGTH_SHORT).show();
        } else if (announcementDesc.isEmpty()) {
            etAnnouncementDesc.requestFocus();
            Toast.makeText(this, "Please Enter Announcement Description", Toast.LENGTH_SHORT).show();
        } else {
            final Map<String, Object> announcementData = new HashMap<>();
            announcementData.put("announcementId", announcementId);
            announcementData.put("announcementTitle", announcementTitle);
            announcementData.put("announcementDesc", announcementDesc);
            announcementData.put("announcementDate", FieldValue.serverTimestamp());
            announcementData.put("kostId", kostId);

            kostRef.document(kostId).collection("Announcement")
                    .document(announcementId)
                    .set(announcementData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminNewAnnouncement.this, "Success Add New Announcement", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

        }

    }
}
