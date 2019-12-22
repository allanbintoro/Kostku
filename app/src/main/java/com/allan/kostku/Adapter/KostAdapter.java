package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Kost;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class KostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Kost> kostList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public void refreshItem(List<Kost> newItems) {
        kostList = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Kost obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public KostAdapter(Context context, List<Kost> kostList) {
        this.kostList = kostList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView tvKostName, tvKostLocation, tvKostType, tvRoomTotal;
        private ImageView imgKost, imgKostType;

        public OriginalViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.lyt_parent);
            tvKostName = (TextView) v.findViewById(R.id.tvKostName);
            tvKostType = (TextView) v.findViewById(R.id.tvKostType);
            tvKostLocation = (TextView) v.findViewById(R.id.tvKostLocation);
            tvRoomTotal = (TextView) v.findViewById(R.id.tvRoomTotal);
            imgKostType = (ImageView) v.findViewById(R.id.kostTypeIcon);
            imgKost = (ImageView) v.findViewById(R.id.imgKost);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kost, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Kost kost = kostList.get(position);
            view.tvKostName.setText(kost.getKostName());
            view.tvKostLocation.setText(kost.getKostLocation());
            view.tvRoomTotal.setText(ResourceManager.getRoomByKostId
                    (ResourceManager.ROOMS, kost.getKostId()).size() + " Rooms");
            if (kost.getKostType().equals("Man")) {
                view.tvKostType.setText(kost.getKostType());
                view.imgKostType.setImageResource(R.drawable.ic_boy);
                view.tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_blue);
            }
            if (kost.getKostType().equals("Woman")) {
                view.tvKostType.setText(kost.getKostType());
                view.imgKostType.setImageResource(R.drawable.ic_woman);
                view.tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_pink);
            } else {
                view.tvKostType.setText(kost.getKostType());
                view.imgKostType.setImageResource(R.drawable.ic_mix);
                view.tvKostType.setBackgroundResource(R.drawable.bg_rounded_corner_green);
            }
            if (kost.getKostImage() != null) {
                Glide.with(ctx)
                        .load(kost.getKostImage())
                        .apply(new RequestOptions()
                                .override(100, 100))
                        .into(view.imgKost);
            }
            view.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, kost, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return kostList.size();
    }

}