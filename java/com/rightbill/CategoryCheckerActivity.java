package com.rightbill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by chaisarfati on 26/07/2019.
 *
 * APPLICATION ICON generously offered by mynamepong
 * on flaticon.com
 */

public class CategoryCheckerActivity extends AppCompatActivity {

    ListView listCategories;
    String[] titles = {"FOOD", "CAR", "HOUSE", "ENTERTAINMENT", "CLOTHES", "HEALTH"};
    int[] images = {R.mipmap.food_icon,
            R.mipmap.car_icon,
            R.mipmap.house_icon,
            R.mipmap.entertainment_icon,
            R.mipmap.clothes_icon,
            R.mipmap.health_icon
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Render
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_checker);

        // Set width and height of this popup activity
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.73) , (int) (height * 0.5178));

        listCategories = (ListView) findViewById(R.id.categories);
        CategoryListAdapter adapter = new CategoryListAdapter(this, titles, images);
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(CategoryCheckerActivity.this, R.layout.row_category, R.id.category_text, titles);
        listCategories.setAdapter(adapter);

        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendBackAndClose(CategoryTransaction.valueOf(titles[position]));
                CategoryCheckerActivity.this.finish();
            }
        });

    }

    private void sendBackAndClose(CategoryTransaction c) {
        Intent intent = new Intent();
        intent.putExtra("category", c);
        setResult(RESULT_OK, intent);
        CategoryCheckerActivity.this.finish();
    }


    class CategoryListAdapter extends ArrayAdapter<String>{
        Context context;
        String[] rTitle;
        int[] rImgs;

        public CategoryListAdapter(Context c, String[] title, int[] imgs){
            super(c, R.layout.row_category, R.id.category_text, title);
            this.context = c;
            this.rTitle = title;
            this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_category, parent, false);

            ImageView images = (ImageView) row.findViewById(R.id.illustration);
            TextView category = (TextView) row.findViewById(R.id.category_text);

            images.setImageResource(rImgs[position]);
            category.setText(rTitle[position]);

            return row;
        }
    }

}
