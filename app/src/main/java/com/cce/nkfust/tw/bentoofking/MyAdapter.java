package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Asus on 2018/4/13.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private List<store_list> store_lists;
    public MyAdapter(Context context, List<store_list> store_lists){
        myInflater = LayoutInflater.from(context);
        this.store_lists = store_lists;
    }


    private class ViewHolder {
        TextView textView21;
        TextView textView24;
        TextView textView32;
        TextView textView33;
        TextView textView34;
        TextView textView35;
        TextView textView36;
        public ViewHolder(TextView textView21 ,TextView textView24 ,TextView textView32 ,TextView textView33 ,TextView textView34 ,TextView textView35 ,TextView textView36){
            this.textView21 = textView21;
            this.textView24 = textView24;
            this.textView32 = textView32;
            this.textView33 = textView33;
            this.textView34 = textView34;
            this.textView35 = textView35;
            this.textView36 = textView36;
        }
    }


    @Override
    public int getCount() {
        return store_lists.size();
    }

    @Override
    public Object getItem(int arg0) {
        return store_lists.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return store_lists.indexOf(getItem(position));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.main_list, null);
            holder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.textView21),
                    (TextView) convertView.findViewById(R.id.textView24),
                    (TextView) convertView.findViewById(R.id.textView32),
                    (TextView) convertView.findViewById(R.id.textView33),
                    (TextView) convertView.findViewById(R.id.textView34),
                    (TextView) convertView.findViewById(R.id.textView35),
                    (TextView) convertView.findViewById(R.id.textView36)
            );
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        store_list store_list =  (store_list)getItem(position);


        holder.textView32.setText(store_list.getPrice());
        holder.textView33.setText(store_list.getStorename());

        holder.textView36.setText(store_list.getDistance());

        return convertView;
    }

}
