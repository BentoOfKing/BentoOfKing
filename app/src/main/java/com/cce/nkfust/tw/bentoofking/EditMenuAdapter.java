package com.cce.nkfust.tw.bentoofking;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by John on 2018/7/22.
 */

class EditMealAdapter extends BaseAdapter {
    private ArrayList<MenuItem> menuItems;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        TextView itemTextView;
    }

    public EditMealAdapter(LayoutInflater inflater, ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.inflater = inflater;
    }

    @Override
    public int getItemViewType(int position) {
        if(menuItems.get(position).Price.equals("-1")){
            return 0;
        }else{
            return 1;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public MenuItem getItem(int i) {
        return menuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.edit_menu_item, null);
            viewHolder.itemTextView = view.findViewById(R.id.itemTextView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        switch (getItemViewType(position)){
            case 0:
                viewHolder.itemTextView.setTypeface(viewHolder.itemTextView.getTypeface(),Typeface.BOLD);
                viewHolder.itemTextView.setText(menuItems.get(position).getName());
                break;
            case 1:
                viewHolder.itemTextView.setTypeface(viewHolder.itemTextView.getTypeface(),Typeface.NORMAL);
                viewHolder.itemTextView.setText(menuItems.get(position).getName()+"，"+menuItems.get(position).Price+"元");
                break;
        }
        return view;
    }
}
