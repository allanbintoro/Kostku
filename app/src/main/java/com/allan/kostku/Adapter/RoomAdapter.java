package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Room;
import com.allan.kostku.R;

import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Room> roomList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public void refreshItem(List<Room> newItems){
        roomList = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Room obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public RoomAdapter(Context context, List<Room> roomList) {
        this.roomList = roomList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout cvRoom;
        public TextView tvRoomName, tvRoomPrice, tvRoomWide, tvRoomStatus;

        public OriginalViewHolder(View v) {
            super(v);
            cvRoom = (LinearLayout) v.findViewById(R.id.cvRoom);
            tvRoomName = (TextView)v.findViewById(R.id.tvRoomName);
            tvRoomStatus = (TextView)v.findViewById(R.id.tvRoomStatus);
            tvRoomPrice = (TextView)v.findViewById(R.id.tvRoomPrice);
            tvRoomWide = (TextView)v.findViewById(R.id.tvRoomWide);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Room room = roomList.get(position);

            view.tvRoomName.setText(room.getRoomName());
            if (room.isRoomStatus()){
                view.tvRoomStatus.setText("Available");
                view.tvRoomStatus.setBackgroundResource(R.drawable.bg_rounded_corner_blue);
            }else{
                view.tvRoomStatus.setText("Unavailable");
                view.tvRoomStatus.setBackgroundResource(R.drawable.bg_rounded_corner_pink);
            }

            view.cvRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, room, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

}