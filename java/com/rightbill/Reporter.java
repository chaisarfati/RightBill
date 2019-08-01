package com.rightbill;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * This object is a bill reporter. That is,
 * it is the object we are going to use to
 * write the transactions of the user to
 * the bill file
 *
 * Created by chaisarfati on 26/07/2019.
 */

public class Reporter {

    private File bill;
    private FileWriter writer;
    private String day, month;

    /**
     * Constructor
     * @param bill
     */
    public Reporter(File bill, String day, String month) {
        this.bill = bill;

        try {
            // FileWriter in append mode
            this.writer = new FileWriter(this.bill, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.day = day;
        this.month = month;
    }

    /**
     * Appends an amount and the corresponding category
     * of the transaction it represents to the bill
     * @param amount
     * @param category
     */
    protected void writeAmount(double amount, CategoryTransaction category){
        String toAppend = day + "/" + month + ";" + Double.toString(amount) + ";" + category.name() + "\n";
        try {
            // Appends the transaction to the bill file
            writer.write(toAppend);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the FileWriter stream
     */
    protected void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
