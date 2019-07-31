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

public class MyBillsActivity extends AppCompatActivity {

    ListView listCurrBill,
    listPrevBills;

    String[] titles_curr, titles_prev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybills);

        listCurrBill = (ListView) findViewById(R.id.listViewCurrent);
        //listPrevBills = (ListView) findViewById(R.id.listViewPrev);

        File currBillDir = new File(this.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/curr_bill/");
        titles_curr = currBillDir.list();
        ArrayAdapter<String> adapterCurr = new ArrayAdapter<String>(MyBillsActivity.this, R.layout.row_bill, R.id.text_bill, titles_curr);
        listCurrBill.setAdapter(adapterCurr);
        listCurrBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyBillsActivity.this, BillShowerActivity.class);
                intent.putExtra("date", MyBillsActivity.this.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/curr_bill/" + titles_curr[position]);
                startActivity(intent);
            }
        });
/*
        File prevBillsDir = new File(this.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/prev_bills/");
        titles_prev = prevBillsDir.list();
        Log.e("prevBillDir len:", Integer.toString(titles_prev.length));
        ArrayAdapter<String> adapterPrev = new ArrayAdapter<String>(MyBillsActivity.this, R.layout.row_bill, R.id.text_bill, titles_prev);
        listCurrBill.setAdapter(adapterPrev);*/

    }
}
