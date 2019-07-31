package com.rightbill;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * This objects stores the configurations
 * of the user and is used to initialize the
 * files and directory that will store the
 * transactions and to change some settings
 * configuration.
 *
 * Created by chaisarfati on 25/07/2019.
 */

public class Configurations implements Parcelable {

    protected final String HOME;
    protected final String CONFIG;
    protected final String CURR_BILL;
    protected final String PREV_BILLS;

    Context context;
    File currentBill, config;
    String date;
    int debitDate;

    public Configurations(Context context) {
        this.context = context;
        HOME = context.getDir("settings", Context.MODE_PRIVATE).getAbsolutePath() + "/";
        CONFIG = HOME + "config";
        CURR_BILL = HOME + "curr_bill/";
        PREV_BILLS = HOME + "prev_bills/";

        Log.e("msg", "home = " + HOME);
        Log.e("msg", "CONFIG = " + CONFIG);
        Log.e("msg", "CURR_BILL = " + CURR_BILL);
        Log.e("msg", "PREV_BILLS = " + PREV_BILLS);

        date = getDate();
        System.out.println("STRING date: " + date);

        openConfiguration();
        System.out.println(config.getAbsolutePath());

        openPrevBills();

        currentBill = openCurrentBill();
        Log.e("msg", "currentBill = " + currentBill.getAbsolutePath());

        System.out.println("INT debit_date: " + debitDate);
        System.out.println("currentBill : " + currentBill.getName());

        if(config != null){
            System.out.println("Reussi, pas null");
        }
    }

    protected Configurations(Parcel in) {
        HOME = in.readString();
        CONFIG = in.readString();
        CURR_BILL = in.readString();
        PREV_BILLS = in.readString();
        date = in.readString();
        debitDate = in.readInt();
    }

    public static final Creator<Configurations> CREATOR = new Creator<Configurations>() {
        @Override
        public Configurations createFromParcel(Parcel in) {
            return new Configurations(in);
        }

        @Override
        public Configurations[] newArray(int size) {
            return new Configurations[size];
        }
    };

    private String getDate(){
        // Get current date
        Date date = new Date(System.currentTimeMillis());

        // Convert to ISO 8601 format
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));

        // Return the date in 'yyyy-mm-dd' format
        return sdf.format(date).split("T")[0];
    }

    /**
     * Opens the config file (create it if not
     * existent already) and initializes the
     * 'config' field
     */
    private void openConfiguration(){
        config = new File(CONFIG);
        try {
            if (config.createNewFile()) { // Config file does not exist, create it
                config.setWritable(true);
                FileWriter fileWriter = new FileWriter(config);
                fileWriter.write("2\n");
                debitDate = 2;
                fileWriter.close();
            } else { // Config file already exist
                // Read the debit date it contains
                BufferedReader reader = new BufferedReader(new FileReader(config));
                // Initialize the 'debitDate' field with the read data
                debitDate = Integer.parseInt(reader.readLine());
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problem occured openConfiguration");
        }

    }

    /**
     * Open the current_bill directory (or creates it if
     * doesn't exist already), and returns the current bill
     * inside this directory (if no bill was present then create
     * one and if outdated bill was present then move it to the
     * prev_bills directory)
     * @return
     */
    private File openCurrentBill(){
        // The bill file to be output
        File outBill;

        // The currBill directory containing the current bill file
        File currBillDirectory = new File(CURR_BILL);

        // If not already exists create the directory
        if(currBillDirectory.mkdir()){
            // Create a new bill file inside it
            outBill = createBill();
            return outBill;
        }

        /* The directory already existed but has no bill
        file inside */

        if (currBillDirectory.list() == null) {
            outBill = createBill();
            return outBill;
        }


        /*
        The directory already exists and there is a
        bill file inside it currently
        We thus need to investigate in order to decide
        whether to archive it and create a new one or
        use it (if it is still up to date)
        */

        // Retrieve this file
        String currBillName = currBillDirectory.list()[0];

        File currBillFile = new File(CURR_BILL + currBillName);

        // Retrieve the date period of this bill
        String[] currDateSplit = currBillName.split("-");
        int currMonth = Integer.parseInt(currDateSplit[0]),
                currYear = Integer.parseInt(currDateSplit[1]);

        // Retrieve today's date
        String[] ourDateSplit = date.split("-");
        String ourDate = ourDateSplit[1] + "-" + ourDateSplit[0]; // MM-yyyy
        int ourMonth = Integer.parseInt(ourDateSplit[1]),
                ourYear = Integer.parseInt(ourDateSplit[0]),
                ourDay = Integer.parseInt(ourDateSplit[2]);

        /* The current bill is the one for this month
        and we are still before the debit date */
        if(currBillName.equals(ourDate) && ourDay < debitDate){
            // Bill Up to date --> Our operations must be appended to this bill
            outBill = currBillFile;
        }
        /* The current bill has this month's date but we have already
        passed the debit date of the month*/
        else if (currBillName.equals(ourDate) && ourDay >= debitDate){
            // Bill not up to date --> To be archived and we
            // must create new bill for the upcoming month
            moveFileToDir(currBillFile.getName(), CURR_BILL, PREV_BILLS);
            outBill = createBill();
        }
        /* The bill represents the expenses of the upcoming month
        */
        else if( (ourMonth + 1 == currMonth && ourYear == currYear)
                || (ourMonth == 12 && currMonth == 1 && ourYear == currYear - 1)){
            // Bill Up to date --> Our operations must be appended to this bill
            outBill = currBillFile;
        }
        // This bill is not up to date
        else{
            // Archive it to prev_bills and create a new one
            moveFileToDir(currBillFile.getName(), CURR_BILL, PREV_BILLS);
            outBill = createBill();
        }
        return outBill;
    }

    /**
     * Moves the input file at location 'pathFrom'
     * to the location 'pathDest'
     * @param fileName
     * @param pathFrom
     * @param pathDest
     */
    private boolean moveFileToDir(String fileName, String pathFrom, String pathDest){
        InputStream inStream;
        OutputStream outStream;

        try{
            // Retrieve the file at pathFrom, ? MAYBE add context.getFiledDir() si ca marche pas comme ca?
            File file = new File(pathFrom + fileName);

            // Creates a virgin copy of file
            File copy = new File(pathDest + fileName);

            // Input streams and buffer to copy the file
            inStream = new FileInputStream(file);
            outStream = new FileOutputStream(copy);
            byte[] buffer = new byte[1024];
            int length;

            // Copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0){
                outStream.write(buffer, 0, length);
            }

            // Close the streams
            inStream.close();
            outStream.close();

            // Delete the original file
            if(file.delete())
                System.out.println("File " + file.getName() + " was copied successfully!");

            return true;

        }catch(IOException e){
            System.err.println("Failed to move " + fileName + " to " + pathDest);
            return false;
        }
    }

    /**
     * Creates a file named "MM-yyyy" in HOME/curr_bill/
     * Method called only when curr_bill already created
     */
    private File createBill() {
        File newBill = new File(CURR_BILL + dateToFileName());
        try {
            newBill.createNewFile();
        } catch (IOException e) {
            System.err.println("Exception occured in createBill()");
        }

        return newBill;
    }

    /**
     * Creates HOME/prev_bills if not already exists
     * @return
     */
    private void openPrevBills(){
        new File(PREV_BILLS).mkdir();
    }

    /**
     * Creates a new bill whose fileName is of the form
     * 'MM-yyyy' according to today's date
     * For example, if today's the 2019-07-25 and the debit
     * date is on the 10th of the month, then the bill
     * fileName will be 08-2019 (for the upcoming month's bill)
     * @return
     */
    private String dateToFileName() {
        // Get an array containing {'yyyy', 'MM', 'dd'}
        String[] split = date.split("-");

        // Number representation of 'dd'
        int day = Integer.parseInt(split[2]);

        // If we have passed the debit day
        if(day >= debitDate) {

            // Create the bill file for the coming month

            /* Special case of december to january : 'MM'
            goes back to 01 and 'yyyy' is incremented
             */
            if (split[1].equals("12")) {
                split[1] = "01";
                int year = Integer.parseInt(split[0]) + 1;
                split[0] = Integer.toString(year);
            } else { // increment regularly the month
                int month = Integer.parseInt(split[1]);
                split[1] = String.format("%02d", month + 1);
            }
        }

        // Else the debit day is still to come so keep this date
        String name = split[1] + "-" + split[0];

        return name;
    }

    /**
     * Change the debit date setting of the user
     * by changing the config file and updating
     * the 'debitDate' field
     * @param debitDate
     */
    public void setDebitDate(int debitDate){
        try {
            if (config.delete()) { // If old config was correctly deleted

                if(config.createNewFile()) { // And the new one is correctly created
                    // Set new debit date according to the new one
                    config = new File(CONFIG);
                    FileWriter fileWriter = new FileWriter(config);
                    fileWriter.write(Integer.toString(debitDate) + "\n");
                    this.debitDate = debitDate;
                }
            } else { // An error occured somewhere
                System.err.println("Setting debit date failed");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Error occcured during setDebitDate");
        }
    }

    public String getTodayDay(){
        return date.split("-")[2];
    }

    public String getTodayMonth(){
        return date.split("-")[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(HOME);
        dest.writeString(CONFIG);
        dest.writeString(CURR_BILL);
        dest.writeString(PREV_BILLS);
        dest.writeString(date);
        dest.writeInt(debitDate);
    }
}
