package com.rightbill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private double amount;
    private CategoryTransaction category;
    private Configurations configurations;
    private EditText inputAmount;
    private Reporter reporter;
    private ImageButton settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amount = 0;
        // Configures the File system environment of the application
        configurations = new Configurations(this);
        // Reporter object to write the transactions to the current bill file
        reporter = new Reporter(configurations.getCurrentBill(),
                configurations.getTodayDay(),
                configurations.getTodayMonth());

        // Initializes edit text to input transaction's amount
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
                        System.err.println("Amount entered too high. Try lower amounts");
                        return false;
                    }
                    inputAmount.getText().clear();
                    return true;
                }
                return false;
            }
        });

        // Initializes the 'settings' button
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
        reporter = new Reporter(configurations.getCurrentBill(),
                configurations.getTodayDay(),
                configurations.getTodayMonth());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // A category has been chosen and returned from the child activity
        if(requestCode == 1 && resultCode == RESULT_OK){
            setCategory((CategoryTransaction) data.getSerializableExtra("category"));
            report();
        }
    }

    /**
     * Writes current amount and category of transaction
     * input by the user
     */
    private void report(){
        // Report everything to the bill
        reporter.writeAmount(this.amount, this.category);
        // Clear the edit text
        inputAmount.getText().clear();
    }

    /*
    Setters
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setCategory(CategoryTransaction category) {
        this.category = category;
    }
}
