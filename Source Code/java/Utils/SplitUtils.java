package com.example.splitup.Utils;

import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class does the task of minimizing the transaction required in order to settle the amount
 * Class will never have an object of it.
 * */
public class SplitUtils {

    private static final String TAG = "SplitUtils";
    // netTransactionMap key is name of the person and its value is person NetAmount
    private static HashMap<String , Integer > netTransactionMap;


    /**Private Cause we don't want to create object of this class*/
    private SplitUtils(){}


    public static ArrayList<TransactionDetails> getSimplifiedTransactionDetails(ArrayList<TransactionDetails> transactionLists) {
        // this will reflect back the changes that are made from mainActivity
        netTransactionMap = new HashMap<>();
        for(int i = 0 ; i < transactionLists.size() ;i++) {
            TransactionDetails details = transactionLists.get(i);

            String debtorsName = details.getEtDebitFrom();
            String creditorsName = details.getEtCreditFrom();
            int amountTransfer = Integer.parseInt(details.getAmount());

            addTransactionNetAmount(debtorsName,creditorsName,amountTransfer);

        }
        debugNetTransaction();
        PersonNetAmount[] personsNetAmount = getPersonsNetAmount();
        return getSimplifiedTransaction(personsNetAmount);
    }
    /** this is just to debug the netAmount for each person **/
    private static void debugNetTransaction() {
        for(Map.Entry<String, Integer> entries : netTransactionMap.entrySet()){
            Log.d(TAG, "displayNetTransaction: "+entries.getKey()+" @ "+entries.getValue());
        }
    }

    /**
     * Here we calculate The net balance of each people in the Transaction
     * eq Swapnil pays Ritesh Rs. 100
     *    Ritesh pays Meena Rs. 50
     *    Meena pays Swapnil RS. 40
     * We see that the Debtors will lose money and the same money is gained by the Creditors
     * So Swapnil net balance is -60
     *    Ritesh net balance is 50
     *    Meena net balance is 10
     * @PARAM debtorsName is the name of person who gives money
     * @PARAM creditorsName is the name of person who receives money
     * @PARAM amountTransfer is the amount the debtor gives to creditor
     * */

    private static void addTransactionNetAmount(String debtorsName, String creditorsName, int amountTransfer) {
        Integer debtorsNetAmount = netTransactionMap.get(debtorsName);
        Integer creditorsNetAmount = netTransactionMap.get(creditorsName);
        // We check for all condition where Debtor and Creditor can be.
        if (debtorsNetAmount == null && creditorsNetAmount == null) {
            netTransactionMap.put(debtorsName,-amountTransfer);
            netTransactionMap.put(creditorsName,amountTransfer);
        }
        else if (debtorsNetAmount != null && creditorsNetAmount == null) {
            netTransactionMap.put(debtorsName,debtorsNetAmount-amountTransfer);
            netTransactionMap.put(creditorsName,amountTransfer);
        }else if(debtorsNetAmount == null && creditorsNetAmount != null) {
            netTransactionMap.put(debtorsName,-amountTransfer);
            netTransactionMap.put(creditorsName,creditorsNetAmount+amountTransfer);
        }else{
            netTransactionMap.put(debtorsName,debtorsNetAmount-amountTransfer);
            netTransactionMap.put(creditorsName,creditorsNetAmount+amountTransfer);
        }

    }

    /***
     * We add PersonNetAmount from HashMap in an array so that we can simplify the transaction
     * between each person.
     * People who are having @PARAM netAmount in minus are the debtors and others are the Creditors
     * We sort the Array so that we can let the greatest debtor to give money to the greatest Creditor
     * */
    private static PersonNetAmount[] getPersonsNetAmount() {
        PersonNetAmount [] netAmount = new PersonNetAmount[netTransactionMap.size()];
        int index = 0;

        for(Map.Entry<String, Integer> entries : netTransactionMap.entrySet()){
            String personName = entries.getKey();
            int personNetAmount = entries.getValue();
            netAmount[index++] = new PersonNetAmount(personName,personNetAmount);
        }

        Arrays.sort(netAmount);
        return netAmount;
    }

    /**
     * Here we minimize the Transaction between people
     * The Debtors are on the left side of the array, and the Creditors on the right
     * we check each of them and find min and then settle each of their netAmount,
     * if Debtor or Creditor netAmount is 0 we move forward.
     * */
    private static ArrayList<TransactionDetails> getSimplifiedTransaction(PersonNetAmount [] personNetAmounts) {
        ArrayList<TransactionDetails> detailsList = new ArrayList<>();
        int low = 0;
        int high = personNetAmounts.length - 1;
        while ( low < high ){
            PersonNetAmount leftPerson = personNetAmounts[low];
            PersonNetAmount rightPerson = personNetAmounts[high];

            int settlementAmount = Math.min(Math.abs(leftPerson.netAmount),rightPerson.netAmount);

            // add the details in the list.

            detailsList.add( new TransactionDetails ( leftPerson.name,rightPerson.name,
                                                     String.valueOf(settlementAmount) ) );

            /* for sure either left or right person netAmount will be zero
               that means, His/her settlement is done and we can move forward */
            leftPerson.netAmount+=settlementAmount;
            rightPerson.netAmount-=settlementAmount;

            if (leftPerson.netAmount != 0){
                personNetAmounts[low] = leftPerson;
                high--; // because right side is settled
            }else if (rightPerson.netAmount != 0){
                personNetAmounts[high] = rightPerson;
                low++; // because left side is settled
            }
            else{
                // When both of them are settle
                low++;
                high--;
            }
        }
        return detailsList;
    }

    /**
     *
     * This class is for single person net Transaction amount.
     * eq Swapnil pays Ritesh Rs. 100
     *    Ritesh pays Meena Rs. 50
     *    Meena pays Swapnil RS. 40
     * NetTransaction for each person is
     *    Swapnil net balance is -60
     *    Ritesh net balance is 50
     *    Meena net balance is 10
     *
     * @PARAM name is name of the person
     * @PARAM netAmount is amount that is generated after {@LINK addTransactionNetAmount()}
     * */
    private static class PersonNetAmount implements Comparable<PersonNetAmount> {
        String name;
        int netAmount;

        PersonNetAmount(String name , int amount) {
            this.name = name;
            this.netAmount = amount;
        }

        // Sorts in ascending to descending order on the bases amount
        @Override
        public int compareTo(PersonNetAmount o) {
            return this.netAmount - o.netAmount;
        }
    }



}
