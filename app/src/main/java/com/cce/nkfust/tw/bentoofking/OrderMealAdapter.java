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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by John on 2018/5/5.
 */

public class OrderMealAdapter extends BaseAdapter {
    private ArrayList<Meal> meal;
    private Context context;
    TextView nameTextView ;
    TextView priceTextView;
    EditText countEditText;
    ImageView reduceImageView;
    ImageView addImageView;
    Meal nowMeal;

    public OrderMealAdapter(Context context, ArrayList<Meal> meal){
        this.meal = meal;
        this.context = context;
    }


    @Override
    public int getCount() {
        return meal.size();
    }

    @Override
    public Object getItem(int i) {
        return meal.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View viewMeal =  LayoutInflater.from(context).inflate(R.layout.order_meal_item,null);
        nowMeal = this.meal.get(i);
        nameTextView = viewMeal.findViewById(R.id.nameTextView);
        nameTextView.setText(nowMeal.getName()+"，售價 "+nowMeal.getPrice()+" 元");
        priceTextView = viewMeal.findViewById(R.id.priceTextView);
        countEditText = viewMeal.findViewById(R.id.countEditText);
        reduceImageView = viewMeal.findViewById(R.id.reduceImageView);
        addImageView = viewMeal.findViewById(R.id.addImageView);
        //reduceImageView.setOnClickListener(new reduceHandler());
        //addImageView.setOnClickListener(new addHandler());
        /*TextWatcher countTextHandler = new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    priceTextView.setText("小計 "+Integer.toString(Integer.parseInt(countEditText.getText().toString()) * Integer.parseInt(nowMeal.getPrice()))+" 元");
                }catch (Exception e){
                    countEditText.setText("小計 0 元");
                }
            }
        };
        countEditText.addTextChangedListener(countTextHandler);*/
        return null;
    }
    class addHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                countEditText.setText(Integer.toString(Integer.parseInt(countEditText.getText().toString()) + 1));
            }catch (Exception e){
                countEditText.setText("1");
            }
        }
    }
    class reduceHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            try {
                if(Integer.parseInt(countEditText.getText().toString()) > 0 ) {
                    countEditText.setText(Integer.toString(Integer.parseInt(countEditText.getText().toString()) - 1));
                }
            }catch (Exception e){
                countEditText.setText("0");
            }
        }
    }
}
