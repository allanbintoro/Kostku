package com.allan.kostku.ActivityMaster;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allan.kostku.Adapter.SliderAdapter;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.DateFormat;

public class MasterRoomDetail extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    SliderAdapter sliderAdapter;
    SliderView sliderView;
    Room room;
    ImageView imgAc, imgFan, imgTv, imgBathroom, imgBed;
    TextView tvRoomName, tvRoomStatus, tvDateIn, tvRoomTenant, tvRoomDesc, tvRoomWide, tvDueDate, tvRoomPrice;
    Button btnEditRoom, btnDeleteRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_room_detail);
        room = (Room) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        sliderView = findViewById(R.id.imgSlider);
        initToolbar();
        initSlider();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Room Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        //RoomFacilities
        imgAc = (ImageView) findViewById(R.id.imgAcStatus);
        imgFan = (ImageView) findViewById(R.id.imgFanStatus);
        imgTv = (ImageView) findViewById(R.id.imgTvStatus);
        imgBathroom = (ImageView) findViewById(R.id.imgBathroomStatus);
        imgBed = (ImageView) findViewById(R.id.imgBedStatus);

        if (!room.getRoomFacilities().isAc()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgAc);
        }
        if (!room.getRoomFacilities().isBed()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgFan);
        }
        if (!room.getRoomFacilities().isTv()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgTv);
        }
        if (!room.getRoomFacilities().isBathroom()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgBathroom);
        }
        if (!room.getRoomFacilities().isBed()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgBed);
        }

        //Room Detail
        tvRoomName = (TextView) findViewById(R.id.tvRoomName);
        tvRoomName.setText(room.getRoomName());
        tvRoomDesc = (TextView) findViewById(R.id.tvRoomDescription);
        tvRoomDesc.setText(room.getRoomDesc());
        tvRoomWide = (TextView) findViewById(R.id.tvRoomWide);
        tvRoomWide.setText(room.getRoomWide());
        tvRoomTenant = (TextView) findViewById(R.id.tvRoomTenant);
        tvDateIn = (TextView) findViewById(R.id.tvDateIn);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        if (room.getUserId() != null) {
            tvRoomTenant.setText(ResourceManager.getUserByUID(ResourceManager.USERS, room.getUserId()));
            tvDateIn.setText("Date In: " + DateFormat.getDateInstance(DateFormat.SHORT).format(room.getDateIn()));
            tvDueDate.setTextColor(Color.RED);
            tvDueDate.setText("Due Date: " + DateFormat.getDateInstance(DateFormat.SHORT).format(room.getDueDate()));
        } else if (room.getUserId() == null) {
            tvRoomTenant.setVisibility(View.GONE);
            tvDateIn.setVisibility(View.GONE);
            tvDueDate.setVisibility(View.GONE);
        }

        tvRoomStatus = (TextView) findViewById(R.id.tvRoomStatus);
        if (room.isRoomStatus()) {
            tvRoomStatus.setText("Available");
        } else if (!room.isRoomStatus()) {
            tvRoomStatus.setText("Unavailable");
        }
        tvRoomPrice = (TextView) findViewById(R.id.tvRoomPrice);
        tvRoomPrice.setText(ResourceManager.currencyFormatter(Double.valueOf(room.getRoomPrice())));

        btnEditRoom = (Button) findViewById(R.id.btnEditRoom);
        btnEditRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MasterRoomDetail.this, MasterEditRoom.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, room);
                startActivityForResult(intent, 2);
            }
        });

        btnDeleteRoom = (Button) findViewById(R.id.btnDeleteRoom);
        btnDeleteRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    private void initSlider() {
        sliderAdapter = new SliderAdapter(this, room.getRoomImage(), room.getBathroomImage2());
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data) {
            if (requestCode == 2) {
                String roomName = data.getStringExtra("roomName");
                String roomDesc = data.getStringExtra("roomDesc");
                String roomWide = data.getStringExtra("roomWide");
                String roomPrice = data.getStringExtra("roomPrice");
                Boolean ac = data.getBooleanExtra("ac", false);
                Boolean fan = data.getBooleanExtra("fan", false);
                Boolean tv = data.getBooleanExtra("tv", false);
                Boolean bathroom = data.getBooleanExtra("bathroom", false);
                Boolean bed = data.getBooleanExtra("bed", false);
                tvRoomName.setText(roomName);
                tvRoomDesc.setText(roomDesc);
                tvRoomWide.setText(roomWide);
                tvRoomPrice.setText(roomPrice);
                if (!ac) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgAc);
                }
                if (ac) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgAc);
                }
                if (!fan) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgFan);
                }
                if (fan) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgFan);
                }
                if (!tv) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgTv);
                }
                if (tv) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgTv);
                }
                if (!bathroom) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgBathroom);
                }
                if (bathroom) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgBathroom);
                }
                if (!bed) {
                    Glide.with(this).load(R.drawable.ic_wrong)
                            .apply(new RequestOptions().override(100, 100)).into(imgBed);
                }
                if (bed) {
                    Glide.with(this).load(R.drawable.ic_check)
                            .apply(new RequestOptions().override(100, 100)).into(imgBed);
                }
            }
        }
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
                kostRef.document(room.getKostId()).collection("Room").document(room.getRoomId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MasterRoomDetail.this, "Success Delete Room", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
