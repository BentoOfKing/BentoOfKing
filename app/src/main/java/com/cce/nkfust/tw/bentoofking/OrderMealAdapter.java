package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by John on 2018/5/5.
 */

public class OrderMealAdapter extends BaseAdapter {
    private ArrayList<OrderMenuItem> orderMenuItem;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder{
        RelativeLayout mealListViewLayout;
        TextView nameTextView ;
        TextView priceTextView;
        TextView countTextView;
    }

    public OrderMealAdapter(LayoutInflater inflater, ArrayList<OrderMenuItem> orderMenuItem){
        this.orderMenuItem = orderMenuItem;
        this.inflater = inflater;
    }
    @Override
    public int getItemViewType(int position) {
        if(orderMenuItem.get(position).Price.equals("-1") || orderMenuItem.get(position).Price.equals("-2")){
            return 0;
        }else{
            return 1;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return orderMenuItem.size();
    }

    @Override
    public OrderMenuItem getItem(int i) {
        return orderMenuItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if(view==null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.order_meal_item,viewGroup,false);
            viewHolder.nameTextView = view.findViewById(R.id.nameTextView);
            viewHolder.priceTextView = view.findViewById(R.id.priceTextView);
            viewHolder.countTextView = view.findViewById(R.id.countTextView);
            viewHolder.mealListViewLayout = view.findViewById(R.id.border);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        viewHolder.mealListViewLayout.setLayoutParams(param);
        viewHolder.mealListViewLayout.setVisibility(View.VISIBLE);
        viewHolder.priceTextView.setVisibility(View.VISIBLE);
        viewHolder.nameTextView.setVisibility(View.VISIBLE);
        viewHolder.countTextView.setVisibility(View.VISIBLE);
        viewHolder.priceTextView.setTextColor(Color.BLACK);
        viewHolder.nameTextView.setTextColor(Color.BLACK);
        viewHolder.countTextView.setTextColor(Color.BLACK);
        switch (getItemViewType(position)) {
            case 0:
                if(getItem(position).Price.equals("-1")) {
                    viewHolder.countTextView.setText("▼");
                }else{
                    viewHolder.countTextView.setText("◀");
                }
                viewHolder.priceTextView.setVisibility(View.GONE);
                viewHolder.nameTextView.setText(getItem(position).getName());
                viewHolder.nameTextView.setTypeface(viewHolder.nameTextView.getTypeface(), Typeface.BOLD);
                break;
            case 1:
                if(getItem(position).getState().equals("0")){
                    viewHolder.mealListViewLayout.setVisibility(View.GONE);
                    param = new RelativeLayout.LayoutParams(0,0);
                    viewHolder.mealListViewLayout.setLayoutParams(param);
                    viewHolder.priceTextView.setVisibility(View.GONE);
                    viewHolder.nameTextView.setVisibility(View.GONE);
                    viewHolder.countTextView.setVisibility(View.GONE);
                }else {
                    if(!getItem(position).getCount().equals("0")){
                        viewHolder.priceTextView.setTextColor(Color.RED);
                        viewHolder.nameTextView.setTextColor(Color.RED);
                        viewHolder.countTextView.setTextColor(Color.RED);
                    }
                    viewHolder.nameTextView.setText(getItem(position).getName() + "，售價 " + getItem(position).getPrice() + " 元");
                    viewHolder.nameTextView.setTypeface(viewHolder.nameTextView.getTypeface(), Typeface.NORMAL);
                    viewHolder.countTextView.setText(getItem(position).getCount() + " 個");
                    viewHolder.priceTextView.setText("小計 " + Integer.parseInt(getItem(position).getPrice()) * Integer.parseInt(getItem(position).getCount()) + " 元");
                }
                break;
        }
        return view;
    }

}
