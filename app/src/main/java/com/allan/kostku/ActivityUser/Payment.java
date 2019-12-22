package com.allan.kostku.ActivityUser;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class Payment extends AppCompatActivity implements TransactionFinishedCallback {
    private static final String TAG = "Payment";
    String transactionId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference transRef = db.collection("Transaction");
    private CollectionReference roomRef = db.collection("Kost");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        setTitle("Payment");

        FancyButton payBtn = findViewById(R.id.button_primary);
        SdkUIFlowBuilder.init()
                .setClientKey("SB-Mid-client-bfA7pEzaHB0V1GuA") // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl("https://midtrans-server.herokuapp.com/checkout.php/") //set merchant url (required) BASE_URL
                .enableLog(true) // enable sdk log (optional)
                .buildSDK();

        UserDetail userDetail = LocalDataHandler.readObject("user_details", UserDetail.class);
        if (userDetail == null) {
            userDetail = new UserDetail();
            userDetail.setUserFullName(ResourceManager.LOGGED_USER.getUserName());
            userDetail.setEmail(ResourceManager.LOGGED_USER.getUserEmail());
            userDetail.setUserId(ResourceManager.LOGGED_USER.getUserId());

            ArrayList<UserAddress> userAddresses = new ArrayList<>();
            UserAddress userAddress = new UserAddress();
            userAddress.setAddress("Jalan Andalas Gang Sebelah No. 1");
            userAddress.setCity("Jakarta");
            userAddress.setAddressType(com.midtrans.sdk.corekit.core.Constants.ADDRESS_TYPE_BOTH);
            userAddress.setZipcode("12345");
            userAddress.setCountry("IDN");
            userAddresses.add(userAddress);
            userDetail.setUserAddresses(userAddresses);
            LocalDataHandler.saveObject("user_details", userDetail);
        }


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Room a = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
                Log.e(TAG, String.valueOf(a.getDueDate()));

                transactionId = transRef.document().getId();
                double TOTAL_AMOUNT = Double.valueOf(a.getRoomPrice());
                final TransactionRequest transactionRequest = new TransactionRequest(transactionId, TOTAL_AMOUNT);
                ItemDetails itemDetails = new ItemDetails("IDKU", TOTAL_AMOUNT, 1, "Kost");

                CustomerDetails customerDetails = new CustomerDetails();
                customerDetails.setFirstName(ResourceManager.LOGGED_USER.getUserName());
                customerDetails.setEmail(ResourceManager.LOGGED_USER.getUserEmail());


                ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
                itemDetailsList.add(itemDetails);

                transactionRequest.setItemDetails(itemDetailsList);

                BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
                transactionRequest.setBillInfoModel(billInfoModel);

                CreditCard creditCardOptions = new CreditCard();
                creditCardOptions.setSaveCard(false);
                creditCardOptions.isSecure();
                creditCardOptions.setBank(BankType.BCA);
                creditCardOptions.setChannel(CreditCard.MIGS);
                transactionRequest.setCreditCard(creditCardOptions);
                MidtransSDK.getInstance().setTransactionRequest(transactionRequest);


                final Map<String, Object> userData = new HashMap<>();
                userData.put("userId", ResourceManager.LOGGED_USER.getUserId());
                userData.put("userName", ResourceManager.LOGGED_USER.getUserName());
                userData.put("userEmail", ResourceManager.LOGGED_USER.getUserEmail());
                userData.put("roomId", a.getRoomId());
                userData.put("kostId", a.getKostId());

                transRef.document(transactionId).set(userData, SetOptions.merge());

                MidtransSDK.getInstance().startPaymentUiFlow(Payment.this);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
        navBar();
    }

    public void navBar() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent a = new Intent(Payment.this, MainActivity.class);
                        a.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.history_menu:
                        Intent b = new Intent(Payment.this, HistoryPayment.class);
                        b.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.payment_menu:
                        break;
                    case R.id.report_menu:
                        Intent c = new Intent(Payment.this, ListReport.class);
                        c.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(c);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.account_menu:
                        Intent d = new Intent(Payment.this, Me.class);
                        d.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(d);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    updateRoomRef();
                    showFeedbackDialog();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
            transRef.document(transactionId).delete();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showFeedbackDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Room a = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
        String kostName = ResourceManager.getKostNameByUid(ResourceManager.KOSTS,a.getKostId());
        final TextView tvFeedbackTitle = (TextView)dialog.findViewById(R.id.tvFeedbackTitle);
        tvFeedbackTitle.setText("Please Rate our Room "+a.getRoomName()+" in "+kostName);
        final EditText etFeedback = (EditText) dialog.findViewById(R.id.etFeedback);
        final AppCompatRatingBar rating_bar = (AppCompatRatingBar) dialog.findViewById(R.id.rating_bar);
        ((AppCompatButton) dialog.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.btnSubmit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = etFeedback.getText().toString().trim();
                if (review.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill review text", Toast.LENGTH_SHORT).show();
                    etFeedback.requestFocus();
                } else if (rating_bar.getRating() == 0) {
                    Toast.makeText(Payment.this, "Please Give Rating", Toast.LENGTH_SHORT).show();
                    rating_bar.requestFocus();
                } else {
                    Room a = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
                    String feedbackId = roomRef.document().getId();
                    final Map<String, Object> feedbackData = new HashMap<>();
                    feedbackData.put("feedbackId", feedbackId);
                    feedbackData.put("kostId", a.getKostId());
                    feedbackData.put("roomId", a.getRoomId());
                    feedbackData.put("userId", a.getUserId());
                    feedbackData.put("feedbackDesc", review);
                    feedbackData.put("feedbackRating", rating_bar.getRating());
                    roomRef.document(a.getKostId())
                            .collection("Feedback")
                            .document(feedbackId)
                            .set(feedbackData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Payment.this, "thank you for giving feedback", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    
                }
                
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void updateRoomRef() {
        Room a = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
        long month = 2592000000L;
        long plusDueDate = a.getDueDate() + month;
        roomRef.document(a.getKostId())
                .collection("Room")
                .document(a.getRoomId())
                .update("dueDate", plusDueDate);
    }

}
