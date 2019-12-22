package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.allan.kostku.Adapter.RoomAdapter;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.twigsntwines.daterangepicker.DatePickerDialog;
import com.twigsntwines.daterangepicker.DateRangePickedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminReport extends AppCompatActivity {
    private RecyclerView rvReportList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference transRef = db.collection("Transaction");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        //Init Toolbar
        initToolbar();
        Button button = (Button) findViewById(R.id.btnMutasi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager(); //Initialize fragment manager
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(); // Create datePickerDialog Instance
                datePickerDialog.show(fragmentManager, "Date Picker"); // Show DatePicker Dialog
                datePickerDialog.setOnDateRangePickedListener(new DateRangePickedListener() {
                    @Override
                    public void OnDateRangePicked(Calendar fromDate, Calendar toDate) {
                        Log.e("From Date", String.valueOf(fromDate.getTimeInMillis()));
                        Log.e("To Date", toDate.getTime().toString());

                    }
                });
            }
        });

        //Init Recycler View
        initRcv();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Report-List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvReportList = findViewById(R.id.rvTransactionList);
        rvReportList.setHasFixedSize(true);
        rvReportList.setLayoutManager(new LinearLayoutManager(this));
    }

//    public void loadMutasi(Calendar fromDate, Calendar toDate) {
//        transRef.whereEqualTo("kostId", kost.getKostId())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        roomList = new ArrayList<>();
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                            Room room = documentSnapshot.toObject(Room.class);
//                            roomList.add(room);
//                        }
//                        roomAdapter = new RoomAdapter(AdminKostDetail.this, roomList);
//                        rvRoomList.setAdapter(roomAdapter);
//
//                        roomAdapter.refreshItem(roomList);
//
//                        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, Room obj, int position) {
//                                Intent intent = new Intent(AdminKostDetail.this, AdminRoomDetail.class);
//                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
//                                startActivity(intent);
//                            }
//                        });
//                    }
//
//                });
//    }

}
