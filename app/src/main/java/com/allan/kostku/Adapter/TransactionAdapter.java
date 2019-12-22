package com.allan.kostku.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.allan.kostku.Model.Room;
import com.allan.kostku.Model.Transaction;
import com.allan.kostku.R;
import com.allan.kostku.ResourceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Transaction> transactionList = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public void refreshItem(List<Transaction> newItems){
        transactionList = newItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Transaction obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public TransactionAdapter(Context context, List<Transaction> roomList) {
        this.transactionList = roomList;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public CardView cvTransaction;
        public TextView tvTransactionId, tvTransactionType, tvTransactionDate, tvTransactionGrossAmount;

        public OriginalViewHolder(View v) {
            super(v);
            cvTransaction = (CardView) v.findViewById(R.id.cvTransaction);
            tvTransactionId = (TextView)v.findViewById(R.id.tvTransactionId);
            tvTransactionDate = (TextView)v.findViewById(R.id.tvTransactionDate);
            tvTransactionGrossAmount = (TextView)v.findViewById(R.id.tvGrossAmount);
            tvTransactionType = (TextView)v.findViewById(R.id.tvTransactionType);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Transaction transaction = transactionList.get(position);

            if (transaction.getTransaction_type().equals("income")){
                view.tvTransactionId.setText("Pay Boarding");
            }
            if (transaction.getTransaction_type().equals("spending")){
                view.tvTransactionId.setText(transaction.getSpending_title());
            }
            view.tvTransactionGrossAmount.setText(ResourceManager.currencyFormatter(Double.valueOf(transaction.getGross_amount())));
            view.tvTransactionDate.setText("Transaction Time: " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(transaction.getSettlement_time()));
            view.tvTransactionType.setText(transaction.getTransaction_type());
            view.cvTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, transaction, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

}