package com.rightbill;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

public class BillShowerActivity extends AppCompatActivity {


    ListView billList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_shower);

        billList = (ListView) findViewById(R.id.billList);
        String fName = getIntent().getStringExtra("date");

        Bill bill = new Bill(fName);

        TransactionListAdapter adapter = new TransactionListAdapter(this, R.layout.row_transaction, bill.getTransactions());
        billList.setAdapter(adapter);
    }
}
