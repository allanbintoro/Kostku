package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.allan.kostku.Model.Kost;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdminNewTransaction extends AppCompatActivity {
    private Kost kost;
    private String kostId, kostName, roomId;
    private Spinner spinnerTransactionType, spinnerRoomList;
    private EditText etSpendingTitle, etSpendingPrice;
    private TextView tvKostName, tvRoomName, tvKostNames, tvUserName, tvRoomPrice, tvRoomStatus, tvRoomDueDate;
    private Button btnSubmit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference transactionRef = db.collection("Transaction");
    private CollectionReference kostRef = db.collection("Kost");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_transaction);
        initToolbar();
        spinnerTransactionType = (Spinner) findViewById(R.id.spinnerTransactionType);
        spinnerRoomList = (Spinner) findViewById(R.id.spinnerRoomName);
        etSpendingTitle = (EditText) findViewById(R.id.etSpendingTitle);
        etSpendingPrice = (EditText) findViewById(R.id.etSpendingPrice);
        tvKostName = (TextView) findViewById(R.id.tvKostName);
        tvRoomName = (TextView) findViewById(R.id.tvRoomName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvRoomPrice = (TextView) findViewById(R.id.tvRoomPrice);
        tvRoomStatus = (TextView) findViewById(R.id.tvRoomStatus);
        tvRoomDueDate = (TextView)findViewById(R.id.tvRoomDueDate);
        btnSubmit = (Button) findViewById(R.id.btnAddTransaction);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTransaction();
            }
        });

        spinnerTransactionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTransactionType.getSelectedItem().toString().equals("income")) {
                    etSpendingTitle.setVisibility(View.GONE);
                    etSpendingPrice.setVisibility(View.GONE);
                    tvKostName.setVisibility(View.VISIBLE);
                    tvKostNames.setVisibility(View.VISIBLE);
                    tvRoomName.setVisibility(View.VISIBLE);
                    spinnerRoomList.setVisibility(View.VISIBLE);
                } else {
                    tvRoomName.setVisibility(View.GONE);
                    spinnerRoomList.setVisibility(View.GONE);
                    tvKostName.setVisibility(View.VISIBLE);
                    tvKostNames.setVisibility(View.VISIBLE);
                    etSpendingTitle.setVisibility(View.VISIBLE);
                    etSpendingPrice.setVisibility(View.VISIBLE);
                    tvUserName.setVisibility(View.GONE);
                    tvRoomPrice.setVisibility(View.GONE);
                    tvRoomStatus.setVisibility(View.GONE);
                    tvRoomDueDate.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> roomList = ResourceManager.getRoomList(ResourceManager.ROOMS, kostId);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AdminNewTransaction.this, android.R.layout.simple_list_item_1, roomList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //Set the layout resource to create the drop down views.
        spinnerRoomList.setAdapter(arrayAdapter); //Set the data to your spinner

        spinnerRoomList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String roomName = spinnerRoomList.getSelectedItem().toString();
                roomId = ResourceManager.getRoomByName(ResourceManager.ROOMS, kostId, roomName);
                Room tenantRoom = ResourceManager.getUserRoomByRoomId(ResourceManager.ROOMS, roomId);
                tvUserName.setVisibility(View.VISIBLE);
                tvUserName.setText(ResourceManager.getUserNameById(ResourceManager.USERS, tenantRoom.getUserId()));
                tvRoomPrice.setVisibility(View.VISIBLE);
                tvRoomPrice.setText(ResourceManager.currencyFormatter(Double.valueOf(tenantRoom.getRoomPrice())));
                tvRoomStatus.setVisibility(View.VISIBLE);
                if (tenantRoom.isRoomStatus()){
                    tvRoomStatus.setText("Room Is Empty");
                    tvRoomDueDate.setVisibility(View.GONE);
                }else{
                    tvRoomStatus.setText("Room Is Not Empty");
                    tvRoomDueDate.setVisibility(View.VISIBLE);
                    tvRoomDueDate.setText("Due Date: " + DateFormat.getDateInstance(DateFormat.SHORT).format(tenantRoom.getDueDate()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        kost = (Kost) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        kostId = kost.getKostId();
        kostName = kost.getKostName();
        tvKostNames = (TextView) findViewById(R.id.tvKostNames);
        tvKostNames.setText(kostName);
    }

    private void addNewTransaction() {
        if (spinnerTransactionType.getSelectedItem().toString().equals("income")) {
            if (spinnerRoomList.getSelectedItem().toString() != null) {
                Room tenantRoom = ResourceManager.getRoomData(ResourceManager.ROOMS, roomId);
                if (tenantRoom.getUserId() == null) {
                    Toast.makeText(this, "Room Is Empty", Toast.LENGTH_SHORT).show();
                    spinnerRoomList.requestFocus();
                } else {
                    final Map<String, Object> transactionData = new HashMap<>();
                    long millis = System.currentTimeMillis();

                    String order_id = transactionRef.document().getId();
                    String status_code = "200"; //200 = Status code for Income
                    transactionData.put("transaction_type", spinnerTransactionType.getSelectedItem().toString());
                    transactionData.put("order_id", order_id);
                    transactionData.put("status_code", status_code);
                    transactionData.put("settlement_time", millis);
                    transactionData.put("gross_amount", tenantRoom.getRoomPrice());
                    transactionData.put("kostId", kostId);
                    transactionData.put("roomId", roomId);
                    transactionRef.document(order_id).set(transactionData, SetOptions.merge());

                    long month = 2592000000L;
                    long plusDueDate = tenantRoom.getDueDate() + month;
                    kostRef.document(kostId)
                            .collection("Room")
                            .document(roomId)
                            .update("dueDate", plusDueDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AdminNewTransaction.this, "Transaction Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        }
        if (spinnerTransactionType.getSelectedItem().toString().equals("spending")) {
            String spendingTitle = etSpendingTitle.getText().toString();
            String spendingPrice = etSpendingPrice.getText().toString();
            if (spendingTitle.isEmpty()) {
                Toast.makeText(this, "Please Input Spending Title", Toast.LENGTH_SHORT).show();
                etSpendingTitle.requestFocus();
            } else if (spendingPrice.isEmpty()) {
                Toast.makeText(this, "Please Input Spending Price", Toast.LENGTH_SHORT).show();
                etSpendingPrice.requestFocus();
            } else {
                final Map<String, Object> spendingData = new HashMap<>();
                long millis = System.currentTimeMillis();
                String order_id = transactionRef.document().getId();
                String status_code = "203"; //203 = Status code for Spending
                spendingData.put("order_id", order_id);
                spendingData.put("status_code", status_code);
                spendingData.put("settlement_time", millis);
                spendingData.put("transaction_type", spinnerTransactionType.getSelectedItem().toString());
                spendingData.put("gross_amount", spendingPrice);
                spendingData.put("spending_title", spendingTitle);
                spendingData.put("kostId", kostId);
                transactionRef.document(order_id).set(spendingData, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdminNewTransaction.this, "Success Add Spending", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        }
    }
}

