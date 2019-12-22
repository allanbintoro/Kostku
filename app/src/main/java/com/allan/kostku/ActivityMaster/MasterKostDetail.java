package com.allan.kostku.ActivityMaster;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Adapter.RoomAdapter;
import com.allan.kostku.Adapter.SliderAdapterKost;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class MasterKostDetail extends AppCompatActivity {
    private Kost kost;
    SliderAdapterKost sliderAdapter;
    SliderView sliderView;
    private RecyclerView rvRoomList;
    private RoomAdapter roomAdapter;
    private Button btnEditKost, btnDeleteKost;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private ArrayList<Room> roomList;
    private TextView tvKostName, tvKostLocation, tvKostDescription, tvKostType, tvKostOwnerName;
    private ImageButton btnAddRoom;
    private ImageView imgWifiStatus, imgParkStatus, img24Status, kostTypeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_kost_detail);
        init();
        initToolbar();
        initRcv();
        sliderView = findViewById(R.id.imgSlider);
        initSlider();
        btnEditKost = (Button) findViewById(R.id.btnEditKost);
        btnEditKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterKostDetail.this, MasterEditKost.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, kost);
                startActivityForResult(intent, 2);
            }
        });
        btnDeleteKost = (Button) findViewById(R.id.btnDeleteKost);
        btnDeleteKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kost Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            kost = (Kost) extras.getSerializable(ResourceManager.PARAM_INTENT_DATA);
        }
        tvKostName = (TextView) findViewById(R.id.tvKostName);
        tvKostName.setText(kost.getKostName());
        tvKostDescription = (TextView) findViewById(R.id.tvKostDescription);
        tvKostDescription.setText(kost.getKostDesc());
        tvKostLocation = (TextView) findViewById(R.id.tvKostLocation);
        tvKostLocation.setText(kost.getKostLocation());
        tvKostType = (TextView) findViewById(R.id.tvKostType);
        kostTypeIcon = (ImageView) findViewById(R.id.kostTypeIcon);
        if (kost.getKostType().equals("Man")) {
            tvKostType.setText(kost.getKostType());
            kostTypeIcon.setImageResource(R.drawable.ic_boy);
            tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_blue);
        }
        if (kost.getKostType().equals("Woman")) {
            tvKostType.setText(kost.getKostType());
            kostTypeIcon.setImageResource(R.drawable.ic_woman);
            tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_pink);
        } else {
            tvKostType.setText(kost.getKostType());
            kostTypeIcon.setImageResource(R.drawable.ic_mix);
            tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_green);
        }

        //Set Kost Facilities
        imgWifiStatus = (ImageView) findViewById(R.id.imgWifiStatus);
        imgParkStatus = (ImageView) findViewById(R.id.imgParkStatus);
        img24Status = (ImageView) findViewById(R.id.img24Status);
        if (!kost.getKostFacilities().isWifi()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgWifiStatus);
        }
        if (!kost.getKostFacilities().isCarPark()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgParkStatus);
        }
        if (!kost.getKostFacilities().isAccess24H()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(img24Status);
        }

        btnAddRoom = (ImageButton) findViewById(R.id.btnAddRoom);
        btnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterKostDetail.this, MasterNewRoom.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, kost);
                startActivity(intent);
            }
        });

    }

    private void initRcv() {
        rvRoomList = findViewById(R.id.rvRoomList);
        rvRoomList.setHasFixedSize(true);
        rvRoomList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSlider() {
        sliderAdapter = new SliderAdapterKost(this, kost.getKostImage(), kost.getParkImage());
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
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
                kostRef.document(kost.getKostId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MasterKostDetail.this, "Success Delete Boarding House", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (requestCode == 2) {
                String kostName = data.getStringExtra("kostName");
                String kostLocation = data.getStringExtra("kostLocation");
                String kostDesc = data.getStringExtra("kostDesc");
                String kostType = data.getStringExtra("kostType");
                Boolean wifi = data.getBooleanExtra("wifi", false);
                Boolean carPark = data.getBooleanExtra("carPark", false);
                Boolean access24H = data.getBooleanExtra("access24H", false);
                tvKostName.setText(kostName);
                tvKostLocation.setText(kostLocation);
                tvKostDescription.setText(kostDesc);
                if (!wifi) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgWifiStatus);
                }
                if (wifi) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgWifiStatus);
                }
                if (!carPark) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgParkStatus);
                }
                if (carPark) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgParkStatus);
                }
                if (!access24H) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(img24Status);
                }
                if (access24H) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(img24Status);
                }
                if (kostType.equals("Man")) {
                    tvKostType.setText(kost.getKostType());
                    kostTypeIcon.setImageResource(R.drawable.ic_boy);
                    tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_blue);
                }
                if (kostType.equals("Woman")) {
                    tvKostType.setText(kost.getKostType());
                    kostTypeIcon.setImageResource(R.drawable.ic_woman);
                    tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_pink);
                } else {
                    tvKostType.setText(kost.getKostType());
                    kostTypeIcon.setImageResource(R.drawable.ic_mix);
                    tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_green);
                }
            }
        }
    }
}
