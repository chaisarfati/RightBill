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

    File bill;
    FileWriter writer;
    String day, mounth;

    /**
     * Constructor
     * @param bill
     */
    public Reporter(File bill, String day, String mounth) {
        this.bill = bill;
        this.bill.setWritable(true);
        try {
            // FileWriter in append mode
            this.writer = new FileWriter(this.bill, true);
        } catch (IOException e) {
            System.err.println("Exception occured in Reporter constructor when open " + this.bill.getAbsolutePath());
            e.printStackTrace();
        }
        this.day = day;
        this.mounth = mounth;
    }

    /**
     * Appends an amount and the corresponding category
     * of the transaction it represents to the bill
     * @param amount
     * @param category
     */
    protected void writeAmount(double amount, CategoryTransaction category){
        System.out.println("writeAmount called");
        String toAppend = day + "/" + mounth + ";" + Double.toString(amount) + ";" + category.name() + "\n";
        try {
            // Appends the transaction
            writer.write(toAppend);
        } catch (IOException e) {
            System.err.println("Exception occured in writing the amount to bill file");
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
            System.err.println("Exception occured in closing the writer of Reporter");
        }
    }
}