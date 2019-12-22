package com.allan.kostku.ActivityUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allan.kostku.ActivityAdminKost.AdminAnnouncementDetail;
import com.allan.kostku.ActivityAdminKost.AdminEditRoom;
import com.allan.kostku.ActivityAdminKost.AdminKostDetail;
import com.allan.kostku.ActivityAdminKost.AdminRoomDetail;
import com.allan.kostku.Adapter.AnnouncementAdapter;
import com.allan.kostku.Adapter.SliderAdapter;
import com.allan.kostku.Model.Announcement;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserRoomActivity extends AppCompatActivity implements TransactionFinishedCallback {
    SliderAdapter sliderAdapter;
    SliderView sliderView;
    ImageView imgAc, imgFan, imgTv, imgBathroom, imgBed;
    TextView tvRoomName, tvRoomStatus, tvDateIn, tvRoomTenant, tvRoomDesc, tvRoomWide, tvDueDate, tvRoomPrice;
    RecyclerView rvAnnouncementList;
    private ArrayList<Announcement> announcementList;
    String transactionId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference transRef = db.collection("Transaction");
    private CollectionReference roomRef = db.collection("Kost");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_room);
        sliderView = findViewById(R.id.imgSlider);
        initToolbar();
        initSlider();
        initView();
        initRcv();

        //Midtrans
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

                MidtransSDK.getInstance().startPaymentUiFlow(UserRoomActivity.this);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        //RoomFacilities
        imgAc = (ImageView) findViewById(R.id.imgAcStatus);
        imgFan = (ImageView) findViewById(R.id.imgFanStatus);
        imgTv = (ImageView) findViewById(R.id.imgTvStatus);
        imgBathroom = (ImageView) findViewById(R.id.imgBathroomStatus);
        imgBed = (ImageView) findViewById(R.id.imgBedStatus);
        Room myRoom = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
        if (!myRoom.getRoomFacilities().isAc()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgAc);
        }
        if (!myRoom.getRoomFacilities().isBed()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgFan);
        }
        if (!myRoom.getRoomFacilities().isTv()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgTv);
        }
        if (!myRoom.getRoomFacilities().isBathroom()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgBathroom);
        }
        if (!myRoom.getRoomFacilities().isBed()) {
            Glide.with(this).load(R.drawable.ic_wrong)
                    .apply(new RequestOptions().override(100, 100)).into(imgBed);
        }

        //Room Detail
        tvRoomName = (TextView) findViewById(R.id.tvRoomName);
        tvRoomName.setText(myRoom.getRoomName());
        tvRoomDesc = (TextView) findViewById(R.id.tvRoomDescription);
        tvRoomDesc.setText(myRoom.getRoomDesc());
        tvRoomWide = (TextView) findViewById(R.id.tvRoomWide);
        tvRoomWide.setText(myRoom.getRoomWide());
        tvRoomTenant = (TextView) findViewById(R.id.tvRoomTenant);
        tvDateIn = (TextView) findViewById(R.id.tvDateIn);
        tvDueDate = (TextView) findViewById(R.id.tvDueDate);
        tvRoomTenant.setText(ResourceManager.getUserByUID(ResourceManager.USERS, myRoom.getUserId()));
        tvDateIn.setText("Date In: " + DateFormat.getDateInstance(DateFormat.SHORT).format(myRoom.getDateIn()));
        tvDueDate.setTextColor(Color.RED);
        tvDueDate.setText("Due Date: " + DateFormat.getDateInstance(DateFormat.SHORT).format(myRoom.getDueDate()));


        tvRoomStatus = (TextView) findViewById(R.id.tvRoomStatus);
        tvRoomStatus.setText("Unavailable");

        tvRoomPrice = (TextView) findViewById(R.id.tvRoomPrice);
        tvRoomPrice.setText(ResourceManager.currencyFormatter(Double.valueOf(myRoom.getRoomPrice())));

    }

    private void initRcv() {
        rvAnnouncementList = findViewById(R.id.rvAnnouncementList);
        rvAnnouncementList.setHasFixedSize(true);
        rvAnnouncementList.setLayoutManager(new LinearLayoutManager(this));
    }


    private void initSlider() {
        Room myRoom = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
        sliderAdapter = new SliderAdapter(this, myRoom.getRoomImage(), myRoom.getBathroomImage2());
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
        String kostName = ResourceManager.getKostNameByUid(ResourceManager.KOSTS, a.getKostId());
        final TextView tvFeedbackTitle = (TextView) dialog.findViewById(R.id.tvFeedbackTitle);
        tvFeedbackTitle.setText("Please Rate our Room " + a.getRoomName() + " in " + kostName);
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
                    Toast.makeText(UserRoomActivity.this, "Please Give Rating", Toast.LENGTH_SHORT).show();
                    rating_bar.requestFocus();
                } else {
                    Room a = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
                    String feedbackId = roomRef.document().getId();
                    long millis = System.currentTimeMillis();
                    final Map<String, Object> feedbackData = new HashMap<>();
                    feedbackData.put("feedbackId", feedbackId);
                    feedbackData.put("kostId", a.getKostId());
                    feedbackData.put("roomId", a.getRoomId());
                    feedbackData.put("userId", a.getUserId());
                    feedbackData.put("feedbackDesc", review);
                    feedbackData.put("feedbackRating", rating_bar.getRating());
                    feedbackData.put("feedbackDate", millis);
                    roomRef.document(a.getKostId())
                            .collection("Feedback")
                            .document(feedbackId)
                            .set(feedbackData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserRoomActivity.this, "thank you for giving feedback", Toast.LENGTH_SHORT).show();
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

    public void loadAnnouncement() {
        Room myRoom = ResourceManager.getUserRoom(ResourceManager.ROOMS, ResourceManager.LOGGED_USER.getUserId());
        roomRef.document(myRoom.getKostId())
                .collection("Announcement")
                .whereEqualTo("kostId", myRoom.getKostId())
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
                        AnnouncementAdapter announcementAdapter = new AnnouncementAdapter(UserRoomActivity.this, announcementList);
                        rvAnnouncementList.setAdapter(announcementAdapter);

                        announcementAdapter.refreshItem(announcementList);

                        announcementAdapter.setOnItemClickListener(new AnnouncementAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, Announcement obj, int position) {
                                Intent intent = new Intent(UserRoomActivity.this, AdminAnnouncementDetail.class);
                                intent.putExtra(ResourceManager.PARAM_INTENT_DATA, obj);
                                startActivity(intent);
                            }
                        });
                    }

                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAnnouncement();
    }
}
