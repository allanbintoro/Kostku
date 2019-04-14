package com.allan.kostku;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class Me extends AppCompatActivity {
    Button btn_edit_profile, btn_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        setTitle("My Profile");

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_feedback = findViewById(R.id.btn_feedback);

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Me.this,ChangePassword.class));
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Me.this,FeedbackDialog.class));
            }
        });



       navBar();
    }

    public void navBar() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent a = new Intent(Me.this, MainActivity.class);
                        a.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.history_menu:
                        Intent b = new Intent(Me.this, HistoryPayment.class);
                        b.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.payment_menu:
                        Intent c = new Intent(Me.this, Payment.class);
                        c.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(c);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.report_menu:
                        Intent d = new Intent(Me.this, ListReport.class);
                        d.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(d);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.account_menu:
                        break;
                }
                return false;
            }
        });
    }

//    private void changePassword(){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.update_password, null);
//        dialogBuilder.setView(dialogView);
//        final EditText newPassword = (EditText) dialogView.findViewById(R.id.newPassword);
//        final Button btn_update = (Button) dialogView.findViewById(R.id.btn_update);
//        dialogBuilder.setTitle("Change Password");
//        AlertDialog alertDialog = dialogBuilder.create();
//        alertDialog.show();
//
//        btn_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final FirebaseUser user;
//                user = FirebaseAuth.getInstance().getCurrentUser();
//                String userNewPassword = newPassword.getText().toString();
//                user.updatePassword(userNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Me.this, "Password Changed", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(Me.this, "Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//            }
//        });
//
//    }

}
