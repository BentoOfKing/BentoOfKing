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

/**
 * Created by John on 2018/5/5.
 */

public class OrderMealAdapter extends BaseAdapter {
    private ArrayList<Meal> meal;
    private Context context;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;



    public static class ViewHolder{
        RelativeLayout mealListViewLayout;
        TextView nameTextView ;
        TextView priceTextView;
        EditText countEditText;
        ImageView reduceImageView;
        ImageView addImageView;
        Meal nowMeal;
    }

    public OrderMealAdapter(LayoutInflater inflater, ArrayList<Meal> meal){
        this.meal = meal;
        //this.context = context;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return meal.size();
    }

    @Override
    public Meal getItem(int i) {
        return meal.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.order_meal_item,null);
            viewHolder.nowMeal = getItem(i);
            viewHolder.nameTextView = view.findViewById(R.id.nameTextView);
            viewHolder.priceTextView = view.findViewById(R.id.priceTextView);
            viewHolder.countEditText = view.findViewById(R.id.countEditText);
            viewHolder.reduceImageView = view.findViewById(R.id.reduceImageView);
            viewHolder.addImageView = view.findViewById(R.id.addImageView);
            viewHolder.mealListViewLayout = view.findViewById(R.id.border);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.nameTextView.setText(getItem(i).getName()+"，售價 "+getItem(i).getPrice()+" 元");



        viewHolder.reduceImageView.setOnClickListener(new reduceHandler());
        viewHolder.addImageView.setOnClickListener(new addHandler());
        TextWatcher countTextHandler = new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!viewHolder.countEditText.getText().toString().equals("")) {
                    try {
                        viewHolder.priceTextView.setText("小計 " + Integer.toString(Integer.parseInt(viewHolder.countEditText.getText().toString()) * Integer.parseInt(viewHolder.nowMeal.getPrice())) + " 元");
                    } catch (Exception e) {
                        viewHolder.countEditText.setText("小計 0 元");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        viewHolder.countEditText.addTextChangedListener(countTextHandler);
        return view;
    }
    class addHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                viewHolder.countEditText.setText(Integer.toString(Integer.parseInt(viewHolder.countEditText.getText().toString()) + 1));
            }catch (Exception e){
                viewHolder.countEditText.setText("1");
            }
        }
    }
    class reduceHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                if(Integer.parseInt(viewHolder.countEditText.getText().toString()) > 0 ) {
                    viewHolder.countEditText.setText(Integer.toString(Integer.parseInt(viewHolder.countEditText.getText().toString()) - 1));
                }
            }catch (Exception e){
                viewHolder.countEditText.setText("0");
            }
        }
    }
}
