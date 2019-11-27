package com.allan.kostku.ActivityMaster;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class MasterNewKost extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_new_kost);
        initToolbar();
        //List Owner Boarding House in Spinner
        spinner = (Spinner) findViewById(R.id.spinnerKostOwner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ResourceManager.USERS_LIST);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set the layout resource to create the drop down views.
        spinner.setAdapter(arrayAdapter); //Set the data to your spinner
        //Button to add new Boarding house
        Button btnAddKost = (Button) findViewById(R.id.btnAddKost);
        //Upload Image
        Intent kostPictureIntent = new Intent(Intent.ACTION_PICK);
        kostPictureIntent.setType("image/*");
        startActivityForResult(kostPictureIntent, 1);

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

    private void addNewKost() {
        EditText etKostName = (EditText) findViewById(R.id.etKostName);
        EditText etKostLocation = (EditText) findViewById(R.id.etKostLocation);
        EditText etKostDesc = (EditText) findViewById(R.id.etKostDesc);
        Spinner spinnerKostType = findViewById(R.id.spinnerKostType);

        //getting the values to save
        String kostName = etKostName.getText().toString().trim();
        String kostLocation = etKostLocation.getText().toString().trim();
        String kostDesc = etKostDesc.getText().toString().trim();
        String kostType = spinnerKostType.getSelectedItem().toString().trim();
        String kostId = kostRef.document().getId();
        String kostOwnerName = spinner.getSelectedItem().toString();
        String kostOwnerId = ResourceManager.getUserByName(ResourceManager.USERS, kostOwnerName);


        //checking if the value is provided

        final Map<String, Object> kostData = new HashMap<>();
        kostData.put("kostId", kostId);
        kostData.put("kostName", kostName);
        kostData.put("kostLocation", kostLocation);
        kostData.put("kostDesc", kostDesc);
        kostData.put("kostType", kostType);

        kostData.put("kostOwnerId", kostOwnerId);

        kostRef.document(kostId).set(kostData);
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1)
//            if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImage = data.getData();
//
//                String filePath = getPath(selectedImage);
//                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
//                image_name_tv.setText(filePath);
//
//                try {
//                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
//                        //FINE
//                    } else {
//                        //NOT IN REQUIRED FORMAT
//                    }
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//    }
//
//    public String getPath(Uri uri) {
//        String[] projection = {MediaColumns.DATA};
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        column_index = cursor
//                .getColumnIndexOrThrow(MediaColumns.DATA);
//        cursor.moveToFirst();
//        imagePath = cursor.getString(column_index);
//
//        return cursor.getString(column_index);
//    }


}

