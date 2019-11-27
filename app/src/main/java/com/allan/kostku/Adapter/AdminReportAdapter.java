package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Report;
import com.allan.kostku.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdminReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Report> reportList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Report obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdminReportAdapter(Context context, List<Report> reportList) {
        this.reportList = reportList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public CardView cvReportItem;
        public TextView tvReportTitle, tvReportDate;

        public OriginalViewHolder(View v) {
            super(v);
            cvReportItem = (CardView) v.findViewById(R.id.cvReportItem);
            tvReportTitle = (TextView) v.findViewById(R.id.tvReportTitle);
            tvReportDate = (TextView) v.findViewById(R.id.tvReportDate);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_list, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Report report = reportList.get(position);
            view.tvReportTitle.setText(report.getReportTitle());
            view.tvReportDate.setText(DateFormat.getDateTimeInstance().format(report.getTimestampCreatedLong()));
            view.cvReportItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, report, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

}