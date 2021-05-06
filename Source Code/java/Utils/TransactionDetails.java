package com.example.splitup.Utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the main class from which we identify who is the money lander
 * and who is the money receiver.
 * This class is used in Recycler View to fill in the details of
 * {etDebitFrom: Contains name of the Debtor that is the money lander}
 * {etCreditFrom: Contains name of the Creditor that is the money receiver}
 * {amount: is the money that is given by debtor to the creditor }
 *
 * */
public class TransactionDetails implements Parcelable {
    String etDebitFrom;
    String etCreditFrom;
    String amount;

    public TransactionDetails() {
    }

    public TransactionDetails(String etDebitFrom, String etCreditFrom, String amount) {
        this.etDebitFrom = etDebitFrom;
        this.etCreditFrom = etCreditFrom;
        this.amount = amount;
    }

    protected TransactionDetails(Parcel in) {
        etDebitFrom = in.readString();
        etCreditFrom = in.readString();
        amount = in.readString();
    }

    public static final Creator<TransactionDetails> CREATOR = new Creator<TransactionDetails>() {
        @Override
        public TransactionDetails createFromParcel(Parcel in) {
            return new TransactionDetails(in);
        }

        @Override
        public TransactionDetails[] newArray(int size) {
            return new TransactionDetails[size];
        }
    };

    public String getEtDebitFrom() {
        return etDebitFrom;
    }

    public void setEtDebitFrom(String etDebitFrom) {
        this.etDebitFrom = etDebitFrom;
    }

    public String getEtCreditFrom() {
        return etCreditFrom;
    }

    public void setEtCreditFrom(String etCreditFrom) {
        this.etCreditFrom = etCreditFrom;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(etDebitFrom);
        dest.writeString(etCreditFrom);
        dest.writeString(amount);
    }
}
