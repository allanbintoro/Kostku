package com.allan.kostku.ActivityAdminKost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.allan.kostku.Model.Announcement;
import com.allan.kostku.Model.Room;
import com.allan.kostku.Model.Transaction;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;

import java.text.DateFormat;

public class AdminTransactionDetail extends AppCompatActivity {
    Transaction transaction;
    TextView tvOrderId, tvSettlementTime,tvUserName, tvRoomName, tvRoomDueDate, tvTransaction,
            tvSpendingTitle,  tvGrossAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transaction_detail);
        initToolbar();
        initview();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transaction Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initview() {
        transaction = (Transaction) getIntent().getExtras().getSerializable(ResourceManager.PARAM_INTENT_DATA);
        tvOrderId = (TextView)findViewById(R.id.tvOrderId);
        tvSettlementTime = (TextView)findViewById(R.id.tvSettlementTime);
        tvUserName = (TextView)findViewById(R.id.tvUserName);
        tvRoomName = (TextView)findViewById(R.id.tvRoomName);
        tvRoomDueDate = (TextView)findViewById(R.id.tvRoomDueDate);
        tvTransaction = (TextView)findViewById(R.id.tvTransaction);
        tvSpendingTitle = (TextView)findViewById(R.id.tvSpendingTitle);
        tvGrossAmount = (TextView)findViewById(R.id.tvGrossAmount);

        if (transaction.getTransaction_type().equals("income")){
            tvOrderId.setText("Transaction Id: "+transaction.getOrder_id());
            tvSettlementTime.setText("Settlement Time"+DateFormat.getDateTimeInstance().format(transaction.getSettlement_time()));
            Room userRoom = ResourceManager.getUserRoomByRoomId(ResourceManager.ROOMS, transaction.getRoomId());
            tvUserName.setText("Tenant Name: "+ResourceManager.getUserNameById(ResourceManager.USERS, userRoom.getUserId()));
            tvRoomName.setText("Room Name: "+userRoom.getRoomName());
            tvRoomDueDate.setText("Due Date: "+DateFormat.getDateInstance().format(userRoom.getDueDate()));
            tvSpendingTitle.setVisibility(View.GONE);
            tvGrossAmount.setText(ResourceManager.currencyFormatter(Double.valueOf(transaction.getGross_amount())));
        }
        if (transaction.getTransaction_type().equals("spending")){
            tvUserName.setVisibility(View.GONE);
            tvRoomDueDate.setVisibility(View.GONE);
            tvRoomName.setVisibility(View.GONE);
            tvTransaction.setText("Spending");
            tvOrderId.setText("Transaction Id: "+transaction.getOrder_id());
            tvSettlementTime.setText("Settlement Time"+DateFormat.getDateTimeInstance().format(transaction.getSettlement_time()));
            tvSpendingTitle.setText(transaction.getSpending_title());
            tvGrossAmount.setText(ResourceManager.currencyFormatter(Double.valueOf(transaction.getGross_amount())));
        }
    }

}
