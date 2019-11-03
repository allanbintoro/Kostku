package com.allan.kostku.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allan.kostku.R;
import com.allan.kostku.ReportData;
import com.allan.kostku.Model.Report;

import java.text.DateFormat;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {

    ArrayList<Report> list;
    Context context;

    public ReportAdapter(ArrayList<Report> list,Context context) {
        this.list = list;
        this.context = context;
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
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Report report = list.get(i);
        myViewHolder.tv_judul.setText(list.get(i).getReportTitle());
        myViewHolder.tv_deskripsi.setText("Deskripsi:"+list.get(i).getReportDesc());
        myViewHolder.tv_nama.setText("Nama Pelapor: "+list.get(i).getUser());
        myViewHolder.tv_date.setText("Tanggal Laporan:"+DateFormat.getDateTimeInstance().format(list.get(i).getTimestampCreatedLong()));
//        myViewHolder.tv_date.setText(DateFormat.getDateInstance(DateFormat.DEFAULT).format(list.get(i).getTimestampCreatedLong()));
        myViewHolder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "test"+ i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ReportData.class);
                intent.putExtra("report_data",report);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_judul,tv_deskripsi, tv_nama, tv_date;
        public LinearLayout listItem;
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