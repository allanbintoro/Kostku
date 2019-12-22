package com.allan.kostku.ActivityAdminKost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.Model.Announcement;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminEditAnnouncement extends AppCompatActivity {
    Announcement announcement;
    EditText etAnnouncementTitle, etAnnouncementDesc;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    String announcementId;
    Button btnUpdateAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_announcement);
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Announcement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        announcement = (Announcement) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        etAnnouncementTitle = (EditText) findViewById(R.id.etAnnouncementTitle);
        etAnnouncementDesc = (EditText) findViewById(R.id.etAnnouncementDesc);
        etAnnouncementTitle.setText(announcement.getAnnouncementTitle());
        etAnnouncementDesc.setText(announcement.getAnnouncementDesc());
        announcementId = announcement.getAnnouncementId();
        btnUpdateAnnouncement = (Button) findViewById(R.id.btnUpdateAnnouncement);
        btnUpdateAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAnnouncement();
            }
        });
    }

    private void updateAnnouncement() {
        //getting the values to save
        final String announcementTitle = etAnnouncementTitle.getText().toString().trim();
        final String announcementDesc = etAnnouncementDesc.getText().toString().trim();
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

            kostRef.document(announcement.getKostId()).collection("Announcement")
                    .document(announcementId)
                    .update(announcementData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminEditAnnouncement.this, "Success Update Announcement", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("announcementTitle", announcementTitle);
                            intent.putExtra("announcementDesc", announcementDesc);
                            setResult(2,intent);
                            finish();
                        }
                    });

        }
    }
}
