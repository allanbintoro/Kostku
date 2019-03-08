package com.allan.kostku;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ActivityTwo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        TextView title = (TextView) findViewById(R.id.activityTitle2);
        title.setText("Activity Two");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent a = new Intent(ActivityTwo.this, MainActivity.class);
                        a.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.history_menu:
                        Intent b = new Intent(ActivityTwo.this, ActivityOne.class);
                        b.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.payment_menu:
                        break;
                    case R.id.report_menu:
                        break;
                    case R.id.account_menu:
                        break;
                }
                return false;
            }
        });
    }
}
