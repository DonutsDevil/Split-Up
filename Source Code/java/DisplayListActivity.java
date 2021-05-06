package com.example.splitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.splitup.Utils.TransactionDetails;
import com.example.splitup.adapter.DisplayListAdapter;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This will display the transaction
 * */
public class DisplayListActivity extends AppCompatActivity {

    private static final String RESULT_ACTIVITY_INTENT_KEY = "transactionDetails";
    private ArrayList<TransactionDetails> netTransactionDetails;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initialize();
        setupRecyclerView();
    }

    private void initialize() {
        netTransactionDetails = getIntent().getExtras().getParcelableArrayList(RESULT_ACTIVITY_INTENT_KEY);
        recyclerView = findViewById(R.id.displayRecycler);
    }

    private void setupRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this,
                                                 LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new DisplayListAdapter(this,netTransactionDetails));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DisplayListActivity.this,ResultActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}