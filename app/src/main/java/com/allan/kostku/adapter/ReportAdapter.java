package com.allan.kostku.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allan.kostku.R;
import com.allan.kostku.model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    private ArrayList<Report> list;
    private Context context;
    private AdapterView.OnItemClickListener listener;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    public ReportAdapter(Context context, ArrayList<Report> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_report,viewGroup, false);
        return new MyViewHolder(view);
    }
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        View v = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
//        return new MyViewHolder(v);
//    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.tv_judul.setText(list.get(i).getReportTitle());
        myViewHolder.tv_deskripsi.setText("Deskripsi:"+list.get(i).getReportDesc());
        myViewHolder.tv_nama.setText("Nama Pelapor: "+list.get(i).getUser());
        myViewHolder.tv_date.setText("Tanggal Laporan:"+DateFormat.getDateTimeInstance().format(list.get(i).getTimestampCreatedLong()));
//        myViewHolder.tv_date.setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(list.get(i).getTimestampCreatedLong()));
    }



    @Override
    public int getItemCount() {
        return list.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_judul,tv_deskripsi, tv_nama, tv_date;
        private LinearLayout listItem;
        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_judul = itemView.findViewById(R.id.list_title);
            tv_deskripsi = itemView.findViewById(R.id.list_desk);
            tv_nama = itemView.findViewById(R.id.list_name);
            tv_date = itemView.findViewById(R.id.list_time);
            listItem = itemView.findViewById(R.id.list_root);
        }
    }
}