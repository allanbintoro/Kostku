package com.allan.kostku.ActivityMaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.allan.kostku.Adapter.KostAdapter;
import com.allan.kostku.Adapter.RoomAdapter;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MasterKostDetail extends AppCompatActivity {
    private Kost kost;
    private RecyclerView rvRoomList;
    private RoomAdapter roomAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private ArrayList<Room> roomList;
    private TextView tvKostName, tvKostLocation, tvKostDescription, tvKostType, tvKostOwnerName;
    private ImageButton btnAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_kost_detail);
        init();
        initToolbar();
        initRcv();
    }

    private void init() {
        kost = (Kost) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        tvKostName = (TextView) findViewById(R.id.tvKostName);
        tvKostName.setText(kost.getKostName());
        tvKostDescription = (TextView) findViewById(R.id.tvKostDescription);
        tvKostDescription.setText(kost.getKostId());

        btnAddRoom = (ImageButton) findViewById(R.id.btnAddRoom);
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MasterKostDetail.this, MasterNewRoom.class));
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kost Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvRoomList = findViewById(R.id.rvRoomList);

        rvRoomList.setHasFixedSize(true);
        rvRoomList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadRoom();

    }

    public void loadRoom() {
        kostRef.document(kost.getKostId())
                .collection("Room")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        roomList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Room room = documentSnapshot.toObject(Room.class);
                            roomList.add(room);
                        }
                        roomAdapter = new RoomAdapter(MasterKostDetail.this, roomList);
                        rvRoomList.setAdapter(roomAdapter);

                        roomAdapter.refreshItem(roomList);

                        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Room obj, int position) {
                                Intent intent = new Intent(MasterKostDetail.this, MasterRoomDetail.class);
                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                                startActivity(intent);
                            }
                        });
                        Log.e("onSuccess: ", roomList + "");
                    }

                });
    }

}
