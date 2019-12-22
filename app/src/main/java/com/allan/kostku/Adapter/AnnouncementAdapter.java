package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Announcement;
import com.allan.kostku.Model.Room;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Announcement> announcementList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public void refreshItem(List<Announcement> newItems){
        announcementList = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Announcement obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AnnouncementAdapter(Context context, List<Announcement> announcementList) {
        this.announcementList = announcementList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAnnouncementTitle, tvAnnouncementDate;
        public LinearLayout linearLayout;


        public OriginalViewHolder(View v) {
            super(v);
            tvAnnouncementTitle = (TextView)v.findViewById(R.id.tvAnnouncementTitle);
            tvAnnouncementDate = (TextView)v.findViewById(R.id.tvAnnouncementDate);
            linearLayout = (LinearLayout) v.findViewById(R.id.lyt_parent);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcementr, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Announcement announcement = announcementList.get(position);
            view.tvAnnouncementTitle.setText(announcement.getAnnouncementTitle());
            view.tvAnnouncementDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(announcement.getAnnouncementDate()));
            view.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, announcement, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

}