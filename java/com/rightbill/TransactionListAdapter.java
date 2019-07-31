package com.rightbill;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by chaisarfati on 30/07/2019.
 */

public class TransactionListAdapter extends ArrayAdapter<Bill.Transaction> {
    private Context mContext;
    int mResource;

    public TransactionListAdapter(Context context, int resource, List<Bill.Transaction> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String date = getItem(position).getDate(),
                amount = getItem(position).getAmount(),
                category = getItem(position).getCategory();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView dateTV = (TextView) convertView.findViewById(R.id.text_date);
        TextView amountTV = (TextView) convertView.findViewById(R.id.text_amount);
        TextView categoryTV = (TextView) convertView.findViewById(R.id.text_category);

        dateTV.setText(date);
        amountTV.setText(amount);
        categoryTV.setText(category);

        return convertView;
    }
}

