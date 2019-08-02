package com.rightbill;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyBillsActivity extends AppCompatActivity {

    ListView listBills;
    String[] titles_bills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybills);

        listBills = (ListView) findViewById(R.id.listBills);

        File currBillDir = new File(this.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/curr_bill/");
        File prevBillsDir = new File(this.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/prev_bills/");
        titles_bills = concatenateTwoArrays(currBillDir.list(), prevBillsDir.list());

        ArrayAdapter<String> adapterCurr = new ArrayAdapter<>(MyBillsActivity.this,
                R.layout.row_bill,
                R.id.text_bill,
                titles_bills);
        listBills.setAdapter(adapterCurr);
        listBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyBillsActivity.this, BillShowerActivity.class);
                intent.putExtra("date", MyBillsActivity.this.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/curr_bill/" + titles_bills[position]);
                startActivity(intent);
            }
        });
    }


    // Custom method to concatenate two String arrays
    protected String[] concatenateTwoArrays(String[] arrayFirst,String[] arraySecond){
        // Initialize an empty list
        List<String> both = new ArrayList<>();
        // Add first array elements to list
        Collections.addAll(both,arrayFirst);
        // Add another array elements to list
        Collections.addAll(both,arraySecond);
        // Convert list to array
        String[] result = both.toArray(new String[both.size()]);
        // Return the result
        return result;
    }

}
