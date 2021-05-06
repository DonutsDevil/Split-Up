package com.example.splitup.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.splitup.R;
import com.example.splitup.Utils.SplitUtils;
import com.example.splitup.Utils.TransactionDetails;
import java.util.ArrayList;

/** This Adapter is used in our MainActivity to ask input from the user */
public class AddTransactionAdapter extends RecyclerView.Adapter<AddTransactionAdapter.Holder> {

    private ArrayList<TransactionDetails> transactionsList; // saves the text from Edit Text
    private final Context context; // MainActivity Context

    public AddTransactionAdapter(Context context,  ArrayList<TransactionDetails> transactionList) {
        this.context = context;
        this.transactionsList = transactionList;
        setDefaultTransactionsList();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutID = R.layout.transaction_recycler_item_layout;
        View view = LayoutInflater.from(context).inflate(layoutID,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        TransactionDetails transactionDetails = transactionsList.get(position);


        holder.setText( transactionDetails.getEtDebitFrom(),
                        transactionDetails.getEtCreditFrom(),
                        transactionDetails.getAmount());

        /** This is to save the data in the transactionList so that when list is scroll Edit Text
         * Text is still there in it
         * Its same for etDebitFrom, etCreditTo, and etAmount
         * */

        holder.etDebitFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                transactionsList.get(holder.getAdapterPosition()).setEtDebitFrom(s.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etCreditTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                transactionsList.get(holder.getAdapterPosition()).setEtCreditFrom(s.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                transactionsList.get(holder.getAdapterPosition()).setAmount(s.toString().toLowerCase().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }

    // Dynamically increasing list which is to be populated by @AddTransactionAdapter
    public void addNewTransaction(int count) {
        transactionsList.add(new TransactionDetails());
        notifyItemInserted(count);
    }

    // returns context of MainActivity
    public Context getContext(){
        return context;
    }

    public ArrayList<TransactionDetails> getTransactionsList(){
        return transactionsList;
    }
    // we add by default 2 entries in out MainActivity
    public void setDefaultTransactionsList() {
        transactionsList = new ArrayList<>();
        for (int i = 0 ; i < 2 ; i++)
            transactionsList.add(new TransactionDetails());
    }

    public void removeTransaction(int position) {
        transactionsList.remove(position);
        notifyItemRemoved(position);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        EditText etDebitFrom;
        EditText etCreditTo;
        EditText etAmount;

        public Holder(@NonNull View itemView) {
            super(itemView);
            etDebitFrom = itemView.findViewById(R.id.ET_debitFrom);
            etCreditTo = itemView.findViewById(R.id.ET_creditTo);
            etAmount = itemView.findViewById(R.id.ET_amount);
        }

        // This will set the debit,credit person name and amount in the EditText view which is in
        // recycler view
        private void setText(String debitFrom, String creditFrom, String amount) {
            etDebitFrom.setText(debitFrom);
            etCreditTo.setText(creditFrom);
            etAmount.setText(amount);
        }

    }

}
