package com.allan.kostku.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allan.kostku.R;
import com.allan.kostku.model.Report;

import java.text.DateFormat;
import java.util.ArrayList;

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.ViewHolder> {

    private ArrayList<Report> list;
    private Context context;

    public AdapterReport(ArrayList<Report> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterReport.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterReport.ViewHolder viewHolder, final int i) {
        viewHolder.tv_judul.setText(list.get(i).getReportTitle());
        viewHolder.tv_deskripsi.setText(list.get(i).getReportDesc());
        viewHolder.tv_nama.setText(list.get(i).getUser());
        viewHolder.tv_date.setText(DateFormat.getDateTimeInstance().format(list.get(i).getTimestampCreatedLong()));
        viewHolder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(viewHolder.ListItem.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                        /*
                          Berpindah Activity pada halaman layout updateData
                          dan mengambil data pada listMahasiswa, berdasarkan posisinya
                          untuk dikirim pada activity selanjutnya
                        */
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNIM", list.get(i).getReportTitle());

                                break;
                            case 1:
                                //Pembahasan selanjutnya mengenai fungsi Delete
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_judul, tv_deskripsi, tv_nama, tv_date;
        private LinearLayout ListItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judul = itemView.findViewById(R.id.list_title);
            tv_deskripsi = itemView.findViewById(R.id.list_desk);
            tv_nama = itemView.findViewById(R.id.list_name);
            tv_date = itemView.findViewById(R.id.list_time);
            ListItem = itemView.findViewById(R.id.list_root);
        }
    }
}
