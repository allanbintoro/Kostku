package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Feedback;
import com.allan.kostku.Model.Transaction;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Feedback> feedbackList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public void refreshItem(List<Feedback> newItems) {
        feedbackList = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Feedback obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout lytParent;
        public TextView tvFeedbackDesc, tvFeedbackRate, tvFeedbackDate;

        public OriginalViewHolder(View v) {
            super(v);
            lytParent = (LinearLayout) v.findViewById(R.id.lyt_parent);
            tvFeedbackDesc = (TextView) v.findViewById(R.id.tvFeedbackDesc);
            tvFeedbackRate = (TextView) v.findViewById(R.id.tvFeedbackRate);
            tvFeedbackDate = (TextView) v.findViewById(R.id.tvFeedbackDate);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feedback, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Feedback feedback = feedbackList.get(position);

            view.tvFeedbackDesc.setText(feedback.getFeedbackDesc());
            view.tvFeedbackRate.setText(String.valueOf(feedback.getFeedbackRating()));
            view.tvFeedbackDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(feedback.getFeedbackDate()));
            view.lytParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, feedback, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

}