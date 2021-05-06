package com.example.splitup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splitup.R;
import com.example.splitup.Utils.TransactionDetails;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class DisplayListAdapter extends RecyclerView.Adapter<DisplayListAdapter.Holder> {
    private final Context context;
    private final ArrayList<TransactionDetails> detailsList;

    public DisplayListAdapter(Context context, ArrayList<TransactionDetails> detailsList) {
        this.detailsList = detailsList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        int layout = R.layout.display_litst_item;
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder holder, int position) {
        TransactionDetails details = detailsList.get(position);
        String debtorName = details.getEtDebitFrom();
        String creditorName = details.getEtCreditFrom();
        String amount = details.getAmount();
        holder.setText(debtorName,creditorName,amount);
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView tvDebitFrom;
        TextView tvCreditTo;
        TextView tvAmount;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvDebitFrom = itemView.findViewById(R.id.TV_debitFrom);
            tvCreditTo = itemView.findViewById(R.id.TV_creditTo);
            tvAmount = itemView.findViewById(R.id.TV_amount);
        }

        // This will set the debit,credit person name and amount in the EditText view which is in
        // recycler view
        private void setText(String debitFrom, String creditFrom, String amount) {
            String amounts = "Rs. "+amount;
            tvDebitFrom.setText(debitFrom);
            tvCreditTo.setText(creditFrom);
            tvAmount.setText(amounts);
        }
    }
}
