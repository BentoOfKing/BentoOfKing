package com.cce.nkfust.tw.bentoofking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreListViewBaseAdapter extends BaseAdapter {
    private String[][] elementData;
    private LayoutInflater inflater;
    private int identionBase;

    public static class ViewHolder{
        ImageView storeIcon;
        LinearLayout storeListViewLayout;
        TextView storeName;
        TextView storeStatus;
        TextView storeDistance;
        TextView storeEvaluation;
        TextView storePrice;
    }

    public StoreListViewBaseAdapter(String[][] data, LayoutInflater inflater){
        this.elementData = data;
        this.inflater = inflater;
        this.identionBase = 100;
    }

    @Override
    public int getCount() {
        return this.elementData.length;
    }

    @Override
    public Object getItem(int position) {
        return this.elementData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.storelistview_item,null);
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

        return convertView;
    }
}
