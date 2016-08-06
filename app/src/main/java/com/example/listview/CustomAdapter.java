package com.example.listview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinsa_000 on 3/29/2016.
 */
public class CustomAdapter extends BaseAdapter {

    private Activity_ListView activity;
    private List<BikeData> bikes;
    private SharedPreferences myPrefs;
    private String prefKey = "JSON Website";

    public CustomAdapter(Activity_ListView activity, List<BikeData> bikes){
        this.activity = activity;
        this.bikes = bikes;
        myPrefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    public int getCount() {
        return bikes.size();
    }

    @Override
    public Object getItem(int position) {
        return bikes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
    Uses the convertView and ViewHolder Pattern to populate each row of the ListView with data from each BikeData
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listview_row_layout, null);
            ViewHolder holder = new ViewHolder();
            holder.model = (TextView)rowView.findViewById(R.id.Model);
            holder.description =(TextView)rowView.findViewById(R.id.Description);
            holder.price = (TextView)rowView.findViewById(R.id.Price);
            holder.image = (ImageView)rowView.findViewById(R.id.imageView1);
            holder.position = position;
            rowView.setTag(holder);
        }
        ViewHolder viewHolder = (ViewHolder) rowView.getTag();
        BikeData bike = bikes.get(position);
        viewHolder.model.setText(bike.Model);
        viewHolder.description.setText(bike.Description);
        viewHolder.price.setText("$ " +bike.Price.toString());
        viewHolder.position = position;

        new DownloadImageTask(viewHolder, position).execute(myPrefs.getString(prefKey, "") + bike.Picture);

        return rowView;
    }

    public class ViewHolder{
        public TextView model;
        public TextView description;
        public TextView price;
        public ImageView image;
        public int position;
    }
}
