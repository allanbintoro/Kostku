package com.allan.kostku.ActivityMaster;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.Model.KostImage;
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

public class MasterNewKost extends AppCompatActivity {
    //Image
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST2 = 2;
    private Button btnImageKost, btnImageKostPark;
    private ImageView imgKost, imgPark;
    private Uri uri1, uri2;
    private StorageReference storageReferences;
    private StorageTask storageTask;
    private String kostId;

    private Switch sParking;
    Boolean wifi, carPark, access24H;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_new_kost);
        initToolbar();
        ui();
        sParking = (Switch) findViewById(R.id.sParking);
        sParking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    imgPark.setVisibility(View.VISIBLE);
                    btnImageKostPark.setVisibility(View.VISIBLE);
                } else {
                    imgPark.setVisibility(View.INVISIBLE);
                    btnImageKostPark.setVisibility(View.INVISIBLE);
                }
            }
        });
        //List Owner Boarding House in Spinner
        spinner = (Spinner) findViewById(R.id.spinnerKostOwner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MasterNewKost.this, android.R.layout.simple_list_item_1, ResourceManager.USERS_LIST);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set the layout resource to create the drop down views.
        spinner.setAdapter(arrayAdapter); //Set the data to your spinner
        //Button to add new Boarding house
        Button btnAddKost = (Button) findViewById(R.id.btnAddKost);
        btnAddKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewKost();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New-Kost");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void ui() {
        btnImageKost = (Button) findViewById(R.id.btnImageKost);
        btnImageKostPark = (Button) findViewById(R.id.btnImageKostPark);
        imgKost = (ImageView) findViewById(R.id.imageKost);
        imgPark = (ImageView) findViewById(R.id.imageKostPark);
        storageReferences = FirebaseStorage.getInstance().getReference("uploads");

        btnImageKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser(PICK_IMAGE_REQUEST);
            }
        });
        btnImageKostPark.setOnClickListener(new View.OnClickListener() {
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
            Glide.with(this).load(uri1).into(imgKost);
        }
        if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri2 = data.getData();
            Glide.with(this).load(uri2).into(imgPark);
        }
    }

    //Upload Image to Firebase Storage and push it to DB
    private void uploadImage1(final Uri uri1) {
        final KostImage kostImage = new KostImage();
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
                            kostImage.setKostImage(downloadUrl.toString());
                            kostRef.document(kostId).set(kostImage, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MasterNewKost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadImage2(final Uri uri2) {
        final KostImage kostImage = new KostImage();
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
                            kostImage.setParkImage(downloadUrl.toString());
                            kostRef.document(kostId).set(kostImage, SetOptions.merge());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MasterNewKost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNewKost() {
        EditText etKostName = (EditText) findViewById(R.id.etKostName);
        EditText etKostLocation = (EditText) findViewById(R.id.etKostLocation);
        EditText etKostDesc = (EditText) findViewById(R.id.etKostDesc);
        Spinner spinnerKostType = findViewById(R.id.spinnerKostType);
        Switch facilitiesWifi = (Switch) findViewById(R.id.sWifi);
        Switch facilitiesCarPark = (Switch) findViewById(R.id.sParking);
        Switch facilitiesAccess24H = (Switch) findViewById(R.id.s24Hours);

        //getting the values to save
        String kostName = etKostName.getText().toString().trim();
        String kostLocation = etKostLocation.getText().toString().trim();
        String kostDesc = etKostDesc.getText().toString().trim();
        String kostType = spinnerKostType.getSelectedItem().toString().trim();
        kostId = kostRef.document().getId();
        String kostOwnerName = spinner.getSelectedItem().toString();
        String kostOwnerId = ResourceManager.getUserByName(ResourceManager.USERS, kostOwnerName);


        //checking if the value is provided
        if (facilitiesWifi.isChecked())
            wifi = true;
        else
            wifi = false;
        if (facilitiesCarPark.isChecked())
            carPark = true;
        else
            carPark = false;
        if (facilitiesAccess24H.isChecked())
            access24H = true;
        else
            access24H = false;
        if (kostName.isEmpty()) {
            etKostName.requestFocus();
            Toast.makeText(this, "Boarding House name Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (kostLocation.isEmpty()) {
            etKostLocation.requestFocus();
            Toast.makeText(this, "Boarding House Location Can't Empty", Toast.LENGTH_SHORT).show();
        } else if (kostDesc.isEmpty()) {
            etKostDesc.requestFocus();
            Toast.makeText(this, "Please fill in a description about your Boarding House", Toast.LENGTH_SHORT).show();
        } else {

            uploadFile();
            final Map<String, Object> kostData = new HashMap<>();
            kostData.put("kostId", kostId);
            kostData.put("kostName", kostName);
            kostData.put("kostLocation", kostLocation);
            kostData.put("kostDesc", kostDesc);
            kostData.put("kostType", kostType);
            kostData.put("kostOwnerId", kostOwnerId);

            final Map<String, Object> kostFacilities = new HashMap<>();
            kostFacilities.put("wifi", wifi);
            kostFacilities.put("carPark", carPark);
            kostFacilities.put("access24H", access24H);

            kostData.put("kostFacilities", kostFacilities);

            kostRef.document(kostId)
                    .set(kostData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MasterNewKost.this, "Success Add New Kost", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MasterNewKost.this, MasterKost.class));
                        }
                    });
        }
    }

}

