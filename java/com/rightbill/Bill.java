package com.rightbill;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chaisarfati on 30/07/2019.
 */

public class Bill {

    private File file;
    private ArrayList<Transaction> transactions;

    public Bill(String path) {
        this.file = new File(path);
        this.transactions = new ArrayList<>(50);
    }

    public void initTransactions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                transactions.add(new Transaction(line));
                System.out.println("line : " + line);
                line = reader.readLine();
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public Object[] toArray(){
        return transactions.toArray();
    }

    public String[] toStringArray(){
        int len = transactions.size();
        String[] out = new String[len];

        for (int i = 0; i < len; i++) {
            out[i] = transactions.get(i).toString();
        }
        return out;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Transaction t : transactions) {
            str.append(t + "\n");
        }
        return str.toString();
    }

    public File getFile() {
        return file;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    class Transaction {
        private String date, amount, category;

        public Transaction(String line) {
            String[] fields = line.split(";");

            if(fields.length != 3){
                System.err.println("Wrong transaction line input. Cannot create Transaction object");
                return;
            }

            date = fields[0];
            amount = fields[1];
            category = fields[2];
        }

        public String getDate() {
            return date;
        }

        public String getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }

        @Override
        public String toString() {
            return date + '\t' + amount + '\t' + category + '\t';
        }

    }
}
