package com.allan.kostku.ActivityAdminKost;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Adapter.AnnouncementAdapter;
import com.allan.kostku.Adapter.FeedbackAdapter;
import com.allan.kostku.Adapter.RoomAdapter;
import com.allan.kostku.Adapter.SliderAdapterKost;
import com.allan.kostku.Adapter.TransactionAdapter;
import com.allan.kostku.Model.Announcement;
import com.allan.kostku.Model.Feedback;
import com.allan.kostku.Model.Kost;
import com.allan.kostku.Model.Room;
import com.allan.kostku.Model.Transaction;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.twigsntwines.daterangepicker.DatePickerDialog;
import com.twigsntwines.daterangepicker.DateRangePickedListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdminKostDetail extends AppCompatActivity {

    private Kost kost;
    SliderAdapterKost sliderAdapter;
    SliderView sliderView;
    Double totalIncome, totalSpending, totalAll;
    private RecyclerView rvRoomList, rvAnnouncementList, rvTransactionList, rvFeedbackList;
    private RoomAdapter roomAdapter;
    private Button btnEditKost, btnDeleteKost, btnMutasi;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference kostRef = db.collection("Kost");
    private CollectionReference transactionRef = db.collection("Transaction");
    private ArrayList<Room> roomList;
    private ArrayList<Announcement> announcementList;
    private ArrayList<Transaction> transactionList;
    private ArrayList<Feedback> feedbacksList;
    private TextView tvKostName, tvKostLocation, tvKostDescription, tvKostType;
    private ImageButton btnAddRoom, btnAddAnnouncement, btnAddTransaction;
    private ImageView imgWifiStatus, imgParkStatus, img24Status, kostTypeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kost_detail);
        init();
        initToolbar();
        initRcv();
        sliderView = findViewById(R.id.imgSlider);
        initSlider();
        btnEditKost = (Button) findViewById(R.id.button);
        btnEditKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKostDetail.this, AdminEditKost.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, kost);
                startActivityForResult(intent, 2);
            }
        });
        btnDeleteKost = (Button) findViewById(R.id.button2);
        btnDeleteKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
        btnMutasi = (Button) findViewById(R.id.btnMutasi);
        btnMutasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager(); //Initialize fragment manager
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(); // Create datePickerDialog Instance
                datePickerDialog.show(fragmentManager, "Date Picker"); // Show DatePicker Dialog
                datePickerDialog.setOnDateRangePickedListener(new DateRangePickedListener() {
                    @Override
                    public void OnDateRangePicked(Calendar fromDate, Calendar toDate) {
                        TextView tvFromDate = (TextView) findViewById(R.id.tvFromDate);
                        tvFromDate.setText("From: " + DateFormat.getDateInstance(DateFormat.LONG).format(fromDate.getTime()));
                        TextView tvToDate = (TextView) findViewById(R.id.tvToDate);
                        tvToDate.setText("To: " + DateFormat.getDateInstance(DateFormat.LONG).format(toDate.getTime()));
                        loadTransaction(fromDate, toDate);
                    }
                });
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
                Intent intent = new Intent(AdminKostDetail.this, AdminNewRoom.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, kost);
                startActivity(intent);
            }
        });

        btnAddAnnouncement = (ImageButton) findViewById(R.id.btnAddAnnouncement);
        btnAddAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKostDetail.this, AdminNewAnnouncement.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, kost);
                startActivity(intent);
            }
        });

        btnAddTransaction = (ImageButton) findViewById(R.id.btnAddTransaction);
        btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminKostDetail.this, AdminNewTransaction.class);
                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, kost);
                startActivity(intent);
            }
        });

    }

    private void initRcv() {
        rvRoomList = findViewById(R.id.rvRoomList);
        rvRoomList.setHasFixedSize(true);
        rvRoomList.setLayoutManager(new LinearLayoutManager(this));

        rvAnnouncementList = findViewById(R.id.rvAnnouncementList);
        rvAnnouncementList.setHasFixedSize(true);
        rvAnnouncementList.setLayoutManager(new LinearLayoutManager(this));

        rvTransactionList = findViewById(R.id.tvTransactionList);
        rvTransactionList.setHasFixedSize(true);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(this));

        rvFeedbackList = findViewById(R.id.rvFeedbackList);
        rvFeedbackList.setHasFixedSize(true);
        rvFeedbackList.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    protected void onStart() {
        super.onStart();
        loadRoom();
        loadAnnouncement();
        loadFeedback();
    }

    public void loadRoom() {
        kostRef.document(kost.getKostId())
                .collection("Room")
                .whereEqualTo("kostId", kost.getKostId())
                .orderBy("roomName", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        roomList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Room room = documentSnapshot.toObject(Room.class);
                            roomList.add(room);
                        }
                        roomAdapter = new RoomAdapter(AdminKostDetail.this, roomList);
                        rvRoomList.setAdapter(roomAdapter);

                        roomAdapter.refreshItem(roomList);

                        roomAdapter.setOnItemClickListener(new RoomAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Room obj, int position) {
                                Intent intent = new Intent(AdminKostDetail.this, AdminRoomDetail.class);
                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                                startActivity(intent);
                            }
                        });
                    }

                });
    }

    public void loadAnnouncement() {
        kostRef.document(kost.getKostId())
                .collection("Announcement")
                .whereEqualTo("kostId", kost.getKostId())
                .orderBy("announcementDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        announcementList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Announcement announcement = documentSnapshot.toObject(Announcement.class);
                            announcementList.add(announcement);
                        }
                        AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(AdminKostDetail.this, announcementList);
                        rvAnnouncementList.setAdapter(announcementAdapter);

                        announcementAdapter.refreshItem(announcementList);

                        announcementAdapter.setOnItemClickListener(new AnnouncementAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Announcement obj, int position) {
                                Intent intent = new Intent(AdminKostDetail.this, AdminAnnouncementDetail.class);
                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                                startActivity(intent);
                            }
                        });
                    }

                });
    }

    public void loadTransaction(Calendar fromDate, Calendar toDate) {
        transactionRef
                .whereEqualTo("kostId", kost.getKostId())
                .whereGreaterThanOrEqualTo("settlement_time", fromDate.getTimeInMillis())
                .whereLessThanOrEqualTo("settlement_time", toDate.getTimeInMillis())
                .orderBy("settlement_time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        totalIncome = 0.0;
                        totalSpending = 0.0;
                        totalAll = 0.0;
                        transactionList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Transaction transaction = documentSnapshot.toObject(Transaction.class);
                            transactionList.add(transaction);
                            if (transaction.getTransaction_type().equals("income")) {
                                totalIncome += Double.valueOf(transaction.getGross_amount());
                            }
                            if (transaction.getTransaction_type().equals("spending")) {
                                totalSpending += Double.valueOf(transaction.getGross_amount());
                            }
                        }
                        if (transactionList.size() == 0) {
                            Toast toast = Toast.makeText(AdminKostDetail.this,"No Transaction", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        TransactionAdapter transactionAdapter = new TransactionAdapter(AdminKostDetail.this, transactionList);
                        rvTransactionList.setAdapter(transactionAdapter);
                        transactionAdapter.refreshItem(transactionList);

                        TextView tvTotalIncome = (TextView) findViewById(R.id.tvTotalIncome);
                        tvTotalIncome.setText("Total Income: " + ResourceManager.currencyFormatter(totalIncome));
                        TextView tvTotalSpending = (TextView) findViewById(R.id.tvTotalSpending);
                        tvTotalSpending.setText("Total Spending: " + ResourceManager.currencyFormatter(totalSpending));
                        totalAll = totalIncome - totalSpending;
                        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
                        if (totalAll < 0) {
                            tvTotal.setText("Loss   " + ResourceManager.currencyFormatter(totalAll));
                            tvTotal.setTextColor(Color.RED);
                            tvTotal.setTextSize(20);
                        }
                        if (totalAll > 0) {
                            tvTotal.setText("Profit   " + ResourceManager.currencyFormatter(totalAll));
                            tvTotal.setTextColor(Color.GREEN);
                            tvTotal.setTextSize(20);
                        }

                        transactionAdapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Transaction obj, int position) {
                                Intent intent = new Intent(AdminKostDetail.this, AdminTransactionDetail.class);
                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                                startActivity(intent);
                            }
                        });
                    }

                });
    }

    public void loadFeedback() {
        String kostId = kost.getKostId();
        kostRef.document(kostId)
                .collection("Feedback")
                .orderBy("feedbackDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        feedbacksList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Feedback feedback = documentSnapshot.toObject(Feedback.class);
                            feedbacksList.add(feedback);
                        }
                        Log.e( "onSuccess: ",feedbacksList+"" );
                        FeedbackAdapter feedbackAdapter = new FeedbackAdapter(AdminKostDetail.this, feedbacksList);
                        rvFeedbackList.setAdapter(feedbackAdapter);
                        feedbackAdapter.refreshItem(feedbacksList);
//                        feedbackAdapter.setOnItemClickListener(new FeedbackAdapter.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, Feedback obj, int position) {
//                                Intent intent = new Intent(AdminKostDetail.this, AdminFeedbackList.class);
//                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
//                                startActivity(intent);
//                            }
//                        });
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
                        Toast.makeText(AdminKostDetail.this, "Success Delete Boarding House", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
