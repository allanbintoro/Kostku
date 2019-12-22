package com.allan.kostku.ActivityAdminKost;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.Model.Room;
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

public class AdminEditRoom extends AppCompatActivity {
    //Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST2 = 2;
    private Button btnImageRoom, btnImageBathroom;
    private ImageView imgRoom, imgBathroom;
    private Uri uri1, uri2;
    private StorageReference storageReferences;
    private StorageTask storageTask;

    private Room room;
    private EditText etRoomName, etRoomWide, etRoomPrice, etRoomDesc;
    private Switch facilitiesAc, facilitiesFan, facilitiesTv, facilitiesBathroom, facilitiesBed;
    private String kostId, roomId;
    private Boolean ac, fan, tv, bathroom, bed;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_room);
        room = (Room) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        kostId = room.getKostId();
        roomId = room.getRoomId();
        initToolbar();
        ui();
        Button btnUpdateRoom = (Button) findViewById(R.id.btnUpdateRoom);
        btnUpdateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoom();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit-Room");
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

        etRoomName = (EditText) findViewById(R.id.etRoomName);
        etRoomWide = (EditText) findViewById(R.id.etRoomWide);
        etRoomDesc = (EditText) findViewById(R.id.etRoomDesc);
        etRoomPrice = (EditText) findViewById(R.id.etRoomPrice);

        facilitiesAc = (Switch) findViewById(R.id.facilitiesKost1);
        facilitiesFan = (Switch) findViewById(R.id.facilitiesKost2);
        facilitiesTv = (Switch) findViewById(R.id.facilitiesKost3);
        facilitiesBathroom = (Switch) findViewById(R.id.facilitiesKost4);
        facilitiesBed = (Switch) findViewById(R.id.facilitiesKost5);

        if (room.getRoomImage() != null) {
            Glide.with(this).load(room.getRoomImage()).into(imgRoom);
        }
        if (room.getBathroomImage2() != null) {
            Glide.with(this).load(room.getBathroomImage2()).into(imgBathroom);
        }

        etRoomName.setText(room.getRoomName());
        etRoomWide.setText(room.getRoomWide());
        etRoomDesc.setText(room.getRoomDesc());
        etRoomPrice.setText(room.getRoomPrice());
        if (room.getRoomFacilities().isAc()) {
            facilitiesAc.setChecked(true);
        } else {
            facilitiesAc.setChecked(false);
        }
        if (room.getRoomFacilities().isFan()) {
            facilitiesFan.setChecked(true);
        } else {
            facilitiesFan.setChecked(false);
        }
        if (room.getRoomFacilities().isTv()) {
            facilitiesTv.setChecked(true);
        } else {
            facilitiesTv.setChecked(false);
        }
        if (room.getRoomFacilities().isBathroom()) {
            facilitiesBathroom.setChecked(true);
        } else {
            facilitiesBathroom.setChecked(false);
        }
        if (room.getRoomFacilities().isBed()) {
            facilitiesBed.setChecked(true);
        } else {
            facilitiesBed.setChecked(false);
        }
    }

    private void uploadFile() {
        if (uri1 != null && uri2 != null) {
            uploadImage1(uri1);
            uploadImage2(uri2);
        }
        if (uri1 != null && uri2 == null) {
            uploadImage1(uri1);
        } else if (uri2 != null && uri1 == null) {
            uploadImage2(uri2);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
                            roomImage.setBathroomImage(room.getBathroomImage2());
                            kostRef.document(kostId).collection("Room").document(roomId).set(roomImage, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminEditRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            roomImage.setRoomImage(room.getRoomImage());
                            roomImage.setBathroomImage(downloadUrl.toString());
                            kostRef.document(kostId).collection("Room").document(roomId).set(roomImage, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminEditRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateRoom() {
        //getting the values to save
        final String roomName = etRoomName.getText().toString().trim();
        final String roomDesc = etRoomDesc.getText().toString().trim();
        final String roomPrice = etRoomPrice.getText().toString().trim();
        final String roomWide = etRoomWide.getText().toString().trim();
        if (facilitiesAc.isChecked())
            ac = true;
        else
            ac = false;
        if (facilitiesFan.isChecked())
            fan = true;
        else
            fan = false;
        if (facilitiesTv.isChecked())
            tv = true;
        else
            tv = false;
        if (facilitiesBathroom.isChecked())
            bathroom = true;
        else
            bathroom = false;
        if (facilitiesBed.isChecked())
            bed = true;
        else
            bed = false;

        //Check Room Name Availability
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
        } else if (cek){
            //checking if the value is provided
            uploadFile();
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

            kostRef.document(kostId).collection("Room").document(roomId)
                    .update(roomData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AdminEditRoom.this, "Success Update Room", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.putExtra("roomName",roomName);
                            intent.putExtra("roomDesc",roomDesc);
                            intent.putExtra("roomWide",roomWide);
                            intent.putExtra("roomPrice",roomPrice);
                            intent.putExtra("ac",ac);
                            intent.putExtra("fan",fan);
                            intent.putExtra("tv",tv);
                            intent.putExtra("bathroom",bathroom);
                            intent.putExtra("bed",bed);
                            setResult(2,intent);
                            finish();
                        }
                    });
        }
    }
}
