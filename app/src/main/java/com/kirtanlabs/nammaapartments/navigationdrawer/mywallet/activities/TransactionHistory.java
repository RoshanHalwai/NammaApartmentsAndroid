package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.adapters.TransactionHistoryAdapter;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.pojo.Transaction;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TRANSACTIONS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_TRANSACTION_REFERENCE;

public class TransactionHistory extends BaseActivity {

    private List<Transaction> transactionList = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_transaction_history;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.transactions;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressIndicator();

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference userTransactionReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference().child(FIREBASE_CHILD_TRANSACTIONS);
        userTransactionReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot transactionUIDs) {
                if (transactionUIDs.exists()) {
                    for (DataSnapshot transactionUIDSnapshot : transactionUIDs.getChildren()) {
                        DatabaseReference transactionReference = PRIVATE_TRANSACTION_REFERENCE.child(transactionUIDSnapshot.getKey());
                        transactionReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                transactionList.add(dataSnapshot.getValue(Transaction.class));
                                if (transactionList.size() == transactionUIDs.getChildrenCount()) {
                                    hideProgressIndicator();
                                    TransactionHistoryAdapter transactionHistoryAdapter = new TransactionHistoryAdapter(TransactionHistory.this, transactionList);
                                    recyclerView.setAdapter(transactionHistoryAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.no_transactions_performed);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
