package com.cce.nkfust.tw.bentoofking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreListView_BaseAdapter extends BaseAdapter {
    private String[][] elementData;
    private LayoutInflater inflater;
    private int identionBase;

    public static class ViewHolder{
        ImageView storeIcon;
        LinearLayout storeListViewLayout;
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
    }

    public StoreListView_BaseAdapter(String[][] data,LayoutInflater inflater){
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
        return null;
    }
}
