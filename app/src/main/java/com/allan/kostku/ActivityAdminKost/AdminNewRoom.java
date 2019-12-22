package com.allan.kostku.ActivityAdminKost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.allan.kostku.ActivityMaster.MasterKostDetail;
import com.allan.kostku.ActivityMaster.MasterNewRoom;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.Model.RoomImage;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminNewRoom extends AppCompatActivity {
    //Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST2 = 2;
    private Button btnImageRoom, btnImageBathroom;
    private ImageView imgRoom, imgBathroom;
    private Uri uri1, uri2;
    private StorageReference storageReferences;
    private StorageTask storageTask;

    Kost kost;
    String kostId, roomId;
    Boolean ac, fan, tv, bathroom, bed;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_room);
        kost = (Kost) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        kostId = kost.getKostId();
        roomId = kostRef.document().getId();
        initToolbar();
        ui();
        final Switch facilitiesRoom4 = (Switch) findViewById(R.id.facilitiesKost4);
        facilitiesRoom4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgBathroom.setVisibility(View.VISIBLE);
                    btnImageBathroom.setVisibility(View.VISIBLE);
                } else {
                    imgBathroom.setVisibility(View.INVISIBLE);
                    btnImageBathroom.setVisibility(View.INVISIBLE);
                }

            }
        });
        Button btnAddRoom = (Button) findViewById(R.id.btnAddRoom);
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRoom();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New-Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ui() {
        btnImageRoom = (Button) findViewById(R.id.btnImageRoom);
        btnImageBathroom = (Button) findViewById(R.id.btnImageBathroom);
        imgRoom = (ImageView) findViewById(R.id.imageRoom);
        imgBathroom = (ImageView) findViewById(R.id.imageBathroom);
        storageReferences = FirebaseStorage.getInstance().getReference("uploads");

        btnImageRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST);
            }
        });
        btnImageBathroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST2);
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (uri1 != null) {
            uploadImage1(uri1);
        } else if (uri2 != null) {
            uploadImage2(uri2);
        } else if (uri1 == null && uri2 == null) {

        }
    }

    //Take pict from gallery
    private void openFileChooser(int Image_Request) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//Take Multiply Picture
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Image_Request);
    }

    //get Image Path
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri1 = data.getData();
            Glide.with(this).load(uri1).into(imgRoom);
        }
        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri2 = data.getData();
            Glide.with(this).load(uri2).into(imgBathroom);
        }
    }

    //Upload Image to Firebase Storage and push it to DB
    private void uploadImage1(final Uri uri1) {
        final RoomImage roomImage = new RoomImage();
        //Image Room Ref
        StorageReference storageReference = storageReferences.child(System.currentTimeMillis()
                + "." + getFileExtension(uri1));

        //Task to Upload Room Image
        storageTask = storageReference.putFile(uri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 5000);
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();

                        if (uri1.equals(uri1)) {
                            roomImage.setRoomImage(downloadUrl.toString());
                            kostRef.document(kostId).collection("Room").document(roomId).set(roomImage, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminNewRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImage2(final Uri uri2) {
        final RoomImage roomImage = new RoomImage();
        //Image Bathroom Ref
        StorageReference storageReferencee = storageReferences.child(System.currentTimeMillis()
                + "." + getFileExtension(uri2));
        //Task To Upload Bathroom image
        storageTask = storageReferencee.putFile(uri2)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 5000);
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();

                        if (uri2.equals(uri2)) {
                            roomImage.setBathroomImage(downloadUrl.toString());
                            kostRef.document(kostId).collection("Room").document(roomId).set(roomImage, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminNewRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNewRoom() {
        EditText etRoomName = (EditText) findViewById(R.id.etRoomName);
        EditText etRoomWide = (EditText) findViewById(R.id.etRoomWide);
        EditText etRoomDesc = (EditText) findViewById(R.id.etRoomDesc);
        EditText etRoomPrice = (EditText) findViewById(R.id.etRoomPrice);

        Switch facilitiesRoom1 = (Switch) findViewById(R.id.facilitiesKost1);
        Switch facilitiesRoom2 = (Switch) findViewById(R.id.facilitiesKost2);
        Switch facilitiesRoom3 = (Switch) findViewById(R.id.facilitiesKost3);
        Switch facilitiesRoom4 = (Switch) findViewById(R.id.facilitiesKost4);
        Switch facilitiesRoom5 = (Switch) findViewById(R.id.facilitiesKost5);

        //getting the values to save
        String roomName = etRoomName.getText().toString().trim();
        String roomDesc = etRoomDesc.getText().toString().trim();
        String roomPrice = etRoomPrice.getText().toString().trim();
        String roomWide = etRoomWide.getText().toString().trim();
        if (facilitiesRoom1.isChecked())
            ac = true;
        else
            ac = false;
        if (facilitiesRoom2.isChecked())
            fan = true;
        else
            fan = false;
        if (facilitiesRoom3.isChecked())
            tv = true;
        else
            tv = false;
        if (facilitiesRoom4.isChecked())
            bathroom = true;
        else
            bathroom = false;
        if (facilitiesRoom5.isChecked())
            bed = true;
        else
            bed = false;

        //Check Room Nama Availability
        boolean cek = ResourceManager.checkRoomName(ResourceManager.ROOMS, kostId, roomName);

        if (!cek) {
            etRoomName.requestFocus();
            Toast.makeText(this, "Sorry the name of the room is used", Toast.LENGTH_SHORT).show();
        }

        if (roomName.isEmpty()) {
            etRoomName.requestFocus();
            Toast.makeText(this, "Room names can't be empty", Toast.LENGTH_SHORT).show();
        } else if (roomDesc.isEmpty()) {
            etRoomDesc.requestFocus();
            Toast.makeText(this, "Descriptions of the room may not be empty", Toast.LENGTH_SHORT).show();
        } else if (roomPrice.isEmpty()) {
            etRoomPrice.requestFocus();
            Toast.makeText(this, "Room Prices Should Not Be Empty", Toast.LENGTH_SHORT).show();
        } else if (roomWide.isEmpty()) {
            etRoomWide.requestFocus();
            Toast.makeText(this, "Room Width Can Not Be Empty", Toast.LENGTH_SHORT).show();
        } else if (cek) {
            //checking if the value is provided

            final Map<String, Object> roomData = new HashMap<>();
            roomData.put("kostId", kostId);
            roomData.put("roomId", roomId);
            roomData.put("roomName", roomName);
            roomData.put("roomDesc", roomDesc);
            roomData.put("roomWide", roomWide);
            roomData.put("roomPrice", roomPrice);
            roomData.put("roomStatus", true);

            Map<String, Object> roomFacilities = new HashMap<>();
            roomFacilities.put("Ac", ac);
            roomFacilities.put("Fan", fan);
            roomFacilities.put("Tv", tv);
            roomFacilities.put("Bathroom", bathroom);
            roomFacilities.put("Bed", bed);

            roomData.put("roomFacilities", roomFacilities);

            uploadFile();

            kostRef.document(kostId).collection("Room").document(roomId)
                    .set(roomData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminNewRoom.this, "Success Add New Room", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminNewRoom.this, AdminKostDetail.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears all previous activities task
                            finish();
                        }
                    });
        }
    }
}
