package com.example.splitup.Utils;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used as util class to get Node and Edge for displaying it in our activity.
 * */
public class Graph {
    private static final String TAG = "Graph";
    // Contains the minimum transaction needed in form of source, destination and amount to be transfer
    private final ArrayList<TransactionDetails> netTransactionDetailsList;
    private static HashMap<String, ArrayList<Edge>> graph;
    // We don't want to create object of this class
    public Graph(ArrayList<TransactionDetails> netTransactionDetailsList){
        this.netTransactionDetailsList = netTransactionDetailsList;
        createGraph();
    }


    public void createGraph(){
        graph = new HashMap<>();

        for(TransactionDetails details : netTransactionDetailsList) {
            String debtorName = details.getEtDebitFrom();
            String creditorName = details.getEtCreditFrom();
            String amountTransfer = details.getAmount();

            Edge edge = new Edge(debtorName,creditorName,amountTransfer);
            ArrayList<Edge> nodes = graph.get(debtorName);

            if (nodes == null) {
                nodes = new ArrayList<>();
            }

            nodes.add(edge);
            graph.put(debtorName,nodes);

        }
    }

    public void displayGraph(){
        for (Map.Entry<String, ArrayList<Edge>> entry : graph.entrySet()) {
            for(Edge edge : entry.getValue()) {
                Log.d(TAG, "displayGraph: "+edge.sourceDebtor+" -> "+edge.destinationCreditor+" @ "+edge.amountTransfer);
            }
        }
    }

    public static class Edge {
        String sourceDebtor;
        String destinationCreditor;
        String amountTransfer;

        public Edge(String sourceDebtor, String destinationCreditor, String amountTransfer) {
            this.sourceDebtor = sourceDebtor;
            this.destinationCreditor = destinationCreditor;
            this.amountTransfer = amountTransfer;
        }

        public String getSourceDebtor() {
            return sourceDebtor;
        }


        public String getDestinationCreditor() {
            return destinationCreditor;
        }

        public String getAmountTransfer() {
            return amountTransfer;
        }

    }

}
