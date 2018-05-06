package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ArrayList<OrderIncludeMeal> meal;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder{
        RelativeLayout mealListViewLayout;
        TextView nameTextView ;
        TextView priceTextView;
        TextView countTextView;
    }

    public OrderMealAdapter(LayoutInflater inflater, ArrayList<OrderIncludeMeal> meal){
        this.meal = meal;
        //this.context = context;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return meal.size();
    }

    @Override
    public OrderIncludeMeal getItem(int i) {
        return meal.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if(view==null){
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.order_meal_item,null);
            viewHolder.nameTextView = view.findViewById(R.id.nameTextView);
            viewHolder.priceTextView = view.findViewById(R.id.priceTextView);
            viewHolder.countTextView = view.findViewById(R.id.countTextView);
            viewHolder.mealListViewLayout = view.findViewById(R.id.border);
            if(!meal.get(position).getCount().equals("0")){

            }
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.countTextView.setText(meal.get(position).getCount()+" 個");
        viewHolder.nameTextView.setText(getItem(position).getMeal().getName()+"，售價 "+getItem(position).getMeal().getPrice()+" 元");

        viewHolder.priceTextView.setText("小計 "+Integer.parseInt(getItem(position).getMeal().getPrice())*Integer.parseInt(getItem(position).getCount())+" 元");
        return view;
    }

}
