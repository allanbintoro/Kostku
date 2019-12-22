package com.allan.kostku.ActivityUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.allan.kostku.ActivityAdminKost.AdminKostDetail;
import com.allan.kostku.ActivityAdminKost.AdminTransactionDetail;
import com.allan.kostku.Adapter.TransactionAdapter;
import com.allan.kostku.Model.Room;
import com.allan.kostku.Model.Transaction;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class UserPaymentHistoryActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference transactionRef = db.collection("Transaction");
    private ArrayList<Transaction> transactionList;
    private RecyclerView rvTransactionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_history);
        initToolbar();
        initRcv();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRcv() {
        rvTransactionList = findViewById(R.id.rvTransactionList);
        rvTransactionList.setHasFixedSize(true);
        rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTransaction();
    }

    public void loadTransaction() {
        Room myRoom = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
        transactionRef
                .whereEqualTo("roomId", myRoom.getRoomId())
                .orderBy("settlement_time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        transactionList = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Transaction transaction = documentSnapshot.toObject(Transaction.class);
                            transactionList.add(transaction);
                        }
                        if (transactionList.size() == 0) {
                            Toast toast = Toast.makeText(UserPaymentHistoryActivity.this, "No Transaction", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        TransactionAdapter transactionAdapter = new TransactionAdapter(UserPaymentHistoryActivity.this, transactionList);
                        rvTransactionList.setAdapter(transactionAdapter);
                        transactionAdapter.refreshItem(transactionList);

                        transactionAdapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Transaction obj, int position) {
                                Intent intent = new Intent(UserPaymentHistoryActivity.this, AdminTransactionDetail.class);
                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                                startActivity(intent);
                            }
                        });
                    }

                });
    }

}
