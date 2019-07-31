package com.rightbill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText inputAmount;
    private ImageButton settings;
    Configurations configurations;
    double amount;
    CategoryTransaction category;
    Reporter reporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = 0;
        configurations = new Configurations(this);
        reporter = new Reporter(configurations.currentBill, configurations.getTodayDay(), configurations.getTodayMonth());

        inputAmount = (EditText) findViewById(R.id.amountEditText);
        inputAmount.requestFocus();
        inputAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    try{
                        setAmount(Double.parseDouble(inputAmount.getText().toString()));
                        // Ask which type of transaction it is in a popup activity
                        Intent intent = new Intent(MainActivity.this, CategoryCheckerActivity.class);
                        startActivityForResult(intent, 1);
                    } catch (NumberFormatException e){
                        // Inform the user that the amount is too high
                        Toast.makeText(MainActivity.this, "Amount entered too high. Try lower amounts", Toast.LENGTH_SHORT);
                        System.err.println("Amount entered too high. Try lower amounts");
                        return false;
                    }
                    inputAmount.getText().clear();
                    return true;
                }
                return false;
            }
        });

        settings = (ImageButton) findViewById(R.id.settingsButton);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("config", configurations);
                reporter.close();
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reporter = new Reporter(configurations.currentBill, configurations.getTodayDay(), configurations.getTodayMonth());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            setCategory((CategoryTransaction) data.getSerializableExtra("category"));
            System.out.println("Returned : " + category);
            report();
        }
    }

    private void report(){
        // Report everything to the bill
        reporter.writeAmount(this.amount, this.category);
        System.out.println("Sent : " + amount + ";" + category);
        reporter.close();
        // Clear the edit text
        inputAmount.getText().clear();
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(CategoryTransaction category) {
        this.category = category;
    }
}
