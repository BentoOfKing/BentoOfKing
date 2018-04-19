package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogListViewBaseAdapter extends BaseAdapter {

    ArrayList<String> actionArrayList;
    LayoutInflater inflater;
    public static class ViewHolder{
        TextView actionItem;
    }

    public DialogListViewBaseAdapter(ArrayList<String> data, LayoutInflater inflater){
        this.actionArrayList = data;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.actionArrayList.size();
    }

    @Override
    public String getItem(int position) { return this.actionArrayList.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.list_item_activity_63_dialog,null);
            viewHolder.actionItem = convertView.findViewById(R.id.textViewForAction);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.actionItem.setText(getItem(position));


        return convertView;
    }









}
