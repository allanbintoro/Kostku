package com.allan.kostku;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allan.kostku.model.Kost;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class KostAdapter extends RecyclerView.Adapter<KostAdapter.KostViewHolder> {

    private static final String TAG = "KostAdapter";


    private Context context;
    private ArrayList<Kost> dataList;

    public KostAdapter(ArrayList<Kost> dataList, Context context) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public KostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_kost, parent, false);
        return new KostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KostViewHolder holder, final int position) {
        final Kost kost = dataList.get(position);
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtUmur.setText(dataList.get(position).getUmur());
        holder.txtFasilitas.setText(dataList.get(position).getFasilitas());
        holder.ll_dataKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DataKost.class);
                intent.putExtra("KostAdapter_data",kost);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class KostViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtUmur, txtFasilitas;
        private LinearLayout ll_dataKost;

        public KostViewHolder(View itemView) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.nama_kost);
            txtUmur = (TextView) itemView.findViewById(R.id.umur_kost);
            txtFasilitas = (TextView) itemView.findViewById(R.id.fasilitas_kost);
            ll_dataKost = (LinearLayout)itemView.findViewById(R.id.ll_datakost);

        }
    }
}