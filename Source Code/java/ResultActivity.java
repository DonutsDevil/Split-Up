package com.example.splitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.splitup.Utils.TransactionDetails;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import dev.bandb.graphview.AbstractGraphAdapter;
import dev.bandb.graphview.graph.Graph;
import dev.bandb.graphview.graph.Node;
import com.example.splitup.Utils.Graph.Edge;
import dev.bandb.graphview.layouts.layered.SugiyamaArrowEdgeDecoration;
import dev.bandb.graphview.layouts.layered.SugiyamaConfiguration;
import dev.bandb.graphview.layouts.layered.SugiyamaLayoutManager;
/**
 * This Activity Will Display the graph on the Screen
 * */
public class ResultActivity extends AppCompatActivity {
    private static final String NET_TRANSACTION_INTENT_KEY = "netDetailsList";
    private static final String TAG = "ResultActivity";
    private RecyclerView graphRecyclerView;
    private ArrayList<TransactionDetails> netDetails;
    private AbstractGraphAdapter<ResultActivity.NodeViewHolder> adapter;
    /** Key is the Creditor Name amd Value contains List of Edge
     * Edge contains debtor name creditor name and amount transfer between them **/
    private HashMap<String, ArrayList<Edge>> receiverAmountMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initialize();
        setupGraphRecyclerView();
        debug();
        // This Graph class is Different Then the Graph class used in this code
        Graph graph = createGraph();
        setUpGraphView(graph);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                startActivity(new Intent(this,MainActivity.class));
                finish();
                return true;
            }
            case R.id.getListViewMenu: {
                Intent displayListIntent = new Intent(this,DisplayListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("transactionDetails",netDetails);
                displayListIntent.putExtras(bundle);
                startActivity(displayListIntent);
                return true;
            }
        }
        if (item.getItemId() == android.R.id.home){
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUpGraphView(Graph graph) {
        adapter = new AbstractGraphAdapter<NodeViewHolder>() {
            @NonNull
            @NotNull
            @Override
            public NodeViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                int layoutId = R.layout.node;
                View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
                return new NodeViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull @NotNull NodeViewHolder holder, int position) {
                String nodeText = Objects.requireNonNull(getNodeData(position)).toString();
                holder.setNodeText(nodeText);
                ArrayList<Edge> receiversAmountList = receiverAmountMap.get(nodeText);
                if (receiversAmountList != null && receiversAmountList.size() > 0) {
                    holder.setReceiverTextView(receiversAmountList);
                }

            }
        };
        adapter.setGraph(graph);
        graphRecyclerView.setAdapter(adapter);
    }


    private void initialize(){
        receiverAmountMap = new HashMap<>();
        graphRecyclerView = findViewById(R.id.recycler);
        netDetails = getIntent().getExtras().getParcelableArrayList(NET_TRANSACTION_INTENT_KEY);
    }

    private void debug() {
        for(TransactionDetails details : netDetails) {
            Log.d(TAG, "onCreate: "+details.getEtDebitFrom()+" -> "+details.getEtCreditFrom()+" @ "+details.getAmount());
        }
    }

    private void setupGraphRecyclerView() {
        SugiyamaConfiguration configuration = new SugiyamaConfiguration.Builder().setLevelSeparation(100).setNodeSeparation(50).build();
        graphRecyclerView.setLayoutManager(new SugiyamaLayoutManager(this, configuration));
        graphRecyclerView.addItemDecoration(new SugiyamaArrowEdgeDecoration());
    }

    /** Graph used here is from lib. */
    private Graph createGraph() {

        Graph graph = new Graph();
        for(TransactionDetails details : netDetails) {
            String debtorName = details.getEtDebitFrom();
            String creditorName = details.getEtCreditFrom();
            String amountTransfer = details.getAmount();
            Node debitNode = new Node(debtorName);
            Node creditNode = new Node(creditorName);
            graph.addEdge(debitNode,creditNode);
            Edge edge = new Edge(debtorName,creditorName,amountTransfer);

            // put debtor, Creditor name and amount in map to display the amount transfer in nodes
            if (!receiverAmountMap.containsKey(creditorName)) {
                receiverAmountMap.put(creditorName,new ArrayList<>());
            }
            ArrayList<Edge> receiversAmountList = receiverAmountMap.get(creditorName);
            receiversAmountList.add(edge);
            receiverAmountMap.put(creditorName,receiversAmountList);
        }
        return graph;
    }

    static class NodeViewHolder extends RecyclerView.ViewHolder {
        TextView nodeTextView;
        TextView receiverTextView;
        public NodeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nodeTextView = itemView.findViewById(R.id.node_textView);
            receiverTextView = itemView.findViewById(R.id.receiveText);
        }

        protected  void setReceiverTextView(ArrayList<Edge> receiversAmountList) {
            receiverTextView.setVisibility(View.VISIBLE);
            StringBuffer sb = new StringBuffer();
            for(Edge edge : receiversAmountList) {
                sb.append("Get ").append(edge.getAmountTransfer()).append(" from ")
                        .append(edge.getSourceDebtor()).append("\n");
            }
            receiverTextView.setText(sb.toString());
        }
        protected void setNodeText(String text) {
            nodeTextView.setText(text);
        }
    }
}