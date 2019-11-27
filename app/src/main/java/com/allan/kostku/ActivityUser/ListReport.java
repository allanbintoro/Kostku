package com.allan.kostku.ActivityUser;

import android.content.Intent;
import androidx.annotation.NonNull;

import com.allan.kostku.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allan.kostku.Adapter.UserReportAdapter;
import com.allan.kostku.Model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class ListReport extends AppCompatActivity {
    private static final String TAG = "ListReport";
    RecyclerView recyclerView;
    DatabaseReference reportRef;
    ArrayList<Report> reportList;
    private FloatingActionButton fab_add;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_report);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reportRef = FirebaseDatabase.getInstance().getReference("Report");
        progressBar = new ProgressBar(this);

        setTitle("Report");
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab_add = findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListReport.this, ReportDialog.class));
            }
        });
        //Recycler VIew
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        navBar();
    }

    public void navBar() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent a = new Intent(ListReport.this, MainActivity.class);
                        a.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.history_menu:
                        Intent b = new Intent(ListReport.this, HistoryPayment.class);
                        b.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.payment_menu:
                        Intent c = new Intent(ListReport.this, Payment.class);
                        c.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(c);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.report_menu:
                        break;
                    case R.id.account_menu:
                        Intent d = new Intent(ListReport.this, Me.class);
                        d.setFlags(FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(d);
                        overridePendingTransition(0, 0);
                        break;
                }
                return false;
            }
        });
    }

    private void loadUser() {
        final String a = firebaseAuth.getCurrentUser().getEmail();
        Query query = reportRef.orderByChild("user").equalTo(a);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    reportList = new ArrayList<>();
                    reportList.clear();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        reportList.add(i.getValue(Report.class));
                    }
                    UserReportAdapter userReportAdapter = new UserReportAdapter(reportList, ListReport.this);
                    recyclerView.setAdapter(userReportAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInformation() {
        reportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportList = new ArrayList<>();
                reportList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    reportList.add(ds.getValue(Report.class));
                }
                UserReportAdapter userReportAdapter = new UserReportAdapter(reportList, ListReport.this);
                recyclerView.setAdapter(userReportAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListReport.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
//        loadUserInformation();
        loadUser();
        //loadFirebase();
    }

    private void loadFirebase() {
        FirebaseRecyclerOptions<Report> options = new FirebaseRecyclerOptions.Builder<Report>()
                .setQuery(reportRef, Report.class)
                .build();
        FirebaseRecyclerAdapter<Report, FindReportViewHolder> adapter = new FirebaseRecyclerAdapter<Report, FindReportViewHolder>(
                options
        ) {
            @Override
            protected void onBindViewHolder(@NonNull FindReportViewHolder findReportViewHolder, final int i, @NonNull Report report) {
                findReportViewHolder.tv_judul.setText(report.getReportTitle());
                findReportViewHolder.tv_deskripsi.setText("Deskripsi:"+report.getReportDesc());
                findReportViewHolder.tv_nama.setText("Nama Pelapor: "+report.getUser());
                findReportViewHolder.tv_date.setText("Tanggal Laporan:"+ DateFormat.getDateTimeInstance().format(report.getTimestampCreated()));
            }

            @NonNull
            @Override
            public FindReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
                FindReportViewHolder viewHolder = new FindReportViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class FindReportViewHolder extends RecyclerView.ViewHolder{
        TextView tv_judul,tv_deskripsi, tv_nama, tv_date;
        public LinearLayout listItem;
        public FindReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judul = itemView.findViewById(R.id.list_title);
            tv_deskripsi = itemView.findViewById(R.id.list_desk);
            tv_nama = itemView.findViewById(R.id.list_name);
            tv_date = itemView.findViewById(R.id.list_time);
            listItem = itemView.findViewById(R.id.list_root);
        }
    }
}
