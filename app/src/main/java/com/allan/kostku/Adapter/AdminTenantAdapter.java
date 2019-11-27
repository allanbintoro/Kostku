package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Report;
import com.allan.kostku.Model.User;
import com.allan.kostku.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdminTenantAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> userList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, User obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdminTenantAdapter(Context context, List<User> userList) {
        this.userList = userList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public CardView cvUserDetail;
        public TextView tvUserFullName, tvUserKtp;

        public OriginalViewHolder(View v) {
            super(v);
            cvUserDetail = (CardView) v.findViewById(R.id.cvUserDetail);
            tvUserFullName = (TextView) v.findViewById(R.id.tvUserName);
            tvUserKtp = (TextView) v.findViewById(R.id.tvUserKtp);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final User user = userList.get(position);
            view.tvUserFullName.setText(user.getUserName());
            view.tvUserKtp.setText(String.valueOf(user.getUserKtp()));
            view.cvUserDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, user, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}