package com.rightbill;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by chaisarfati on 28/07/2019.
 */

public class SettingsActivity extends Activity {

    private Configurations configurations;
    private EditText debitDate;
    private Button myBills;
    private int newDebitDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        configurations = getIntent().getParcelableExtra("config");
        System.out.println("eh bien : " + configurations.getConfig());

        debitDate = (EditText) findViewById(R.id.editText5);
        debitDate.setHint(Integer.toString(configurations.getDebitDate()));
        debitDate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    int newDebitDate = Integer.parseInt(debitDate.getText().toString());

                    Log.i("newDebitdate:", newDebitDate+"");

                    if(newDebitDate < 1 || newDebitDate > 30){
                        Log.i("trop grosse newdebit", newDebitDate+"");
                        Toast.makeText(getApplicationContext(), "The debit date must be between 1 and 30", Toast.LENGTH_SHORT);
                        debitDate.getText().clear();
                        return false;
                    } else {
                        SettingsActivity.this.newDebitDate = newDebitDate;

                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure to change debit date ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                configurations.setDebitDate(SettingsActivity.this.newDebitDate);
                                debitDate.setHint(Integer.toString(configurations.getDebitDate()));
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                        Log.i("lol", configurations.getDebitDate() + "");
                        return true;
                    }
                }
                return false;
            }
        });

        myBills = (Button) findViewById(R.id.button_mybills);
        myBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MyBillsActivity.class));
            }
        });
    }
}
