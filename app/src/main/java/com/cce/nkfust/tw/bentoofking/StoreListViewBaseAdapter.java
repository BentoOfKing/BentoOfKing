package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StoreListViewBaseAdapter extends BaseAdapter {
    private ArrayList<store_list> elementData;
    private LayoutInflater inflater;
    private int identionBase;
    private Context context;

    public static class ViewHolder{
        ImageView storeIcon;
        LinearLayout storeListViewLayout;
        TextView storeName;
        TextView storeStatus;
        TextView storeDistance;
        TextView storeEvaluation;
        TextView storePrice;
    }

    public StoreListViewBaseAdapter(Context context,ArrayList<store_list> data, LayoutInflater inflater){
        this.elementData = data;
        this.inflater = inflater;
        this.identionBase = 100;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.elementData.size();
    }

    @Override
    public store_list getItem(int position) { return this.elementData.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.storelistview_item,null);
            viewHolder.storeIcon = convertView.findViewById(R.id.storeIcon);
            viewHolder.storeName = convertView.findViewById(R.id.storeName);
            viewHolder.storeDistance = convertView.findViewById(R.id.storeDistance);
            viewHolder.storeEvaluation = convertView.findViewById(R.id.storeScore);
            viewHolder.storePrice = convertView.findViewById(R.id.storePrice);
            viewHolder.storeStatus = convertView.findViewById(R.id.storeStatus);
            viewHolder.storeListViewLayout = convertView.findViewById(R.id.border);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.storeName.setText(getItem(position).getStorename());
        viewHolder.storePrice.setText("平均價位:"+getItem(position).getPrice());
        viewHolder.storeStatus.setText(getItem(position).getStatus());
        viewHolder.storeEvaluation.setText("評價:"+getItem(position).getEvaluation());
        if(!getItem(position).getDistance().equals("null"))
            viewHolder.storeDistance.setText("距離:" + String.format("%.1f", Double.valueOf(getItem(position).getDistance())) + "公里");
        else
            viewHolder.storeDistance.setText("");
        Thread thread = new Thread (new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getBitmapFromURL(getItem(position).getImageURL());
                ((Activity)StoreListViewBaseAdapter.this.context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setImageBitmap(viewHolder,bitmap);
                    }
                });
            }
        });
        thread.start();
        //等待將會使滑動的時候lag
        /*try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return convertView;
    }


    private void setImageBitmap(ViewHolder viewHolder,Bitmap bitmap){
        viewHolder.storeIcon.setImageBitmap(bitmap);
    }


    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
