package com.cce.nkfust.tw.bentoofking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StoreListViewBaseAdapter extends BaseAdapter {
    private ArrayList<store_list> elementData;
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

    public StoreListViewBaseAdapter(ArrayList<store_list> data, LayoutInflater inflater){
        this.elementData = data;
        this.inflater = inflater;
        this.identionBase = 100;
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
        viewHolder.storeName.setText("店家名稱:"+getItem(position).getStorename());
        viewHolder.storePrice.setText("平均價位:"+getItem(position).getPrice());
        viewHolder.storeStatus.setText("狀態:"+getItem(position).getStatus());
        viewHolder.storeEvaluation.setText("評價:"+getItem(position).getEvaluation());
        viewHolder.storeDistance.setText("距離:"+getItem(position).getDistance());

        return convertView;
    }
}
