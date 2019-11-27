package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.User;
import com.allan.kostku.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> userList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public void refreshItem(List<User> newItems){
        userList = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, User obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public UserAdapter(Context context, List<User> userList) {
        this.userList = userList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public TextView tvUserName, tvUserStatus;
        private ImageView ivUser;

        public OriginalViewHolder(View v) {
            super(v);
            linearLayout = (LinearLayout) v.findViewById(R.id.lyt_parent);
            tvUserName = (TextView) v.findViewById(R.id.tvUserName);
            tvUserStatus = (TextView)v.findViewById(R.id.tvUserStatus);
            ivUser = (ImageView)v.findViewById(R.id.ivUser);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final User user = userList.get(position);
            view.tvUserName.setText(user.getUserName());
            if (user.getUserType().equals("1")){
                view.tvUserStatus.setText("Admin");
            } else if(user.getUserType().equals("2")){
                view.tvUserStatus.setText("Boarding Owner");
            }else{
                view.tvUserStatus.setText("Boarding Tenant");
            }
            view.linearLayout.setOnClickListener(new View.OnClickListener() {
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