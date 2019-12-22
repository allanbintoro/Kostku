package com.allan.kostku.ActivityAdminKost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.allan.kostku.Model.Announcement;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;

public class AdminAnnouncementDetail extends AppCompatActivity {
    Announcement announcement;
    TextView tvAnnouncementTitle, tvAnnouncementDate, tvAnnouncementDesc;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_announcement_detail);
        initToolbar();
        initview();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Announcement Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initview() {
        announcement = (Announcement) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        tvAnnouncementTitle = (TextView) findViewById(R.id.tvAnnouncementTitle);
        tvAnnouncementTitle.setText(announcement.getAnnouncementTitle());
        tvAnnouncementDate = (TextView) findViewById(R.id.tvAnnouncementDate);
        tvAnnouncementDate.setText(DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(announcement.getAnnouncementDate()));
        tvAnnouncementDesc = (TextView) findViewById(R.id.tvAnnouncementDetail);
        tvAnnouncementDesc.setText(announcement.getAnnouncementDesc());

        Button btnEditAnnouncement = (Button) findViewById(R.id.btnEditAnnouncement);
        btnEditAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminAnnouncementDetail.this, AdminEditAnnouncement.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, announcement);
                startActivityForResult(intent, 2);
            }
        });

        Button btnDeleteAnnouncement = (Button) findViewById(R.id.btnDeleteAnnouncement);
        btnDeleteAnnouncement.setText("Delete Announcement");
        btnDeleteAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void showDeleteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialog.findViewById(R.id.btn_confirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kostRef.document(announcement.getKostId())
                        .collection("Announcement")
                        .document(announcement.getAnnouncementId())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminAnnouncementDetail.this, "Success Delete Announcement", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (requestCode == 2) {
                String announcementTitle = data.getStringExtra("announcementTitle");
                String announcementDesc = data.getStringExtra("announcementDesc");
                tvAnnouncementTitle.setText(announcementTitle);
                tvAnnouncementDesc.setText(announcementDesc);
            }
        }
    }
}
