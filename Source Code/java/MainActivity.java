package com.example.splitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.splitup.Utils.Graph;
import com.example.splitup.Utils.SplitUtils;
import com.example.splitup.Utils.SwipeToDeleteCallback;
import com.example.splitup.Utils.TransactionDetails;
import com.example.splitup.adapter.AddTransactionAdapter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AddTransactionAdapter adapter;
    // Contains Details about debtorName, CreditorName, and Amount
    private ArrayList<TransactionDetails> transactionList;
    // This is maintain in order for {@adapter} to know that where is the new insertion done.
    private int recyclerCount;

    private LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize needed global variables
        initialize();
        setupRecyclerView();
    }

    private void initialize() {
        transactionList = new ArrayList<>();
        recyclerCount = 2;
        manager = new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL,false);
        adapter = new AddTransactionAdapter(this, transactionList);
    }

    private void setupRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.addTransactionRecycler);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Swipe left or right  will delete it from the adapter.
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            // Adds a new Transaction
            case R.id.menu_add_transaction : {
                recyclerCount++;
                adapter.addNewTransaction(recyclerCount);
                return true;
            }
            case R.id.menu_settle : {
                transactionList = adapter.getTransactionsList();

                /** Check is user as leave any of the Edit Text Filed empty or not
                 * and if empty we don't want to send it to Result Activity */
                if (!isValidData()) {
                    return false;
                }

                debug();
                // This will give us the minimum Transaction we can do to settle the amount.
                ArrayList<TransactionDetails> details = SplitUtils.getSimplifiedTransactionDetails(transactionList);

                Intent resultActivityIntent = new Intent(this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("netDetailsList",details);
                resultActivityIntent.putExtras(bundle);
                startActivity(resultActivityIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    // Test code to verify Received Transaction List from the AddTransactionAdapter
    private void debug() {
        for(TransactionDetails d : transactionList) {
            Log.d(TAG, "debug: "+d.getEtDebitFrom()+" -> "+d.getEtCreditFrom()+" @ "+d.getAmount());
        }
    }
    /** Get The  total items in adapter and then check whether each EditTextView is Filled with data */
    private boolean isValidData() {
        int count  = adapter.getItemCount();
        for(int position  = 0 ; position < count ; position++) {
            View view = manager.findViewByPosition(position);
            if(checkIfEmpty(view)) {
                return false;
            }
        }
        return true;
    }
    /** Checks for Each view Edit Text whether empty or not and if empty return true*/
    private boolean checkIfEmpty(View view) {
        EditText etDebit = view.findViewById(R.id.ET_debitFrom);
        EditText etCredit = view.findViewById(R.id.ET_creditTo);
        EditText etAmount = view.findViewById(R.id.ET_amount);
        if(checkIfEditTextEmpty(etDebit) ||
                checkIfEditTextEmpty(etCredit) ||
                checkIfEditTextEmpty(etAmount) ) {
            return true;
        }
        return false;
    }

    /** Sets error to the  first Empty text view */
    private boolean checkIfEditTextEmpty(EditText et) {
        if (et.getText().toString().trim().isEmpty()) {
            et.setError("Required");
            return true;
        }
        return false;
    }

}