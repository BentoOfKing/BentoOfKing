package com.cce.nkfust.tw.bentoofking;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class StorePhotoPagerAdapter extends PagerAdapter {

    ArrayList<StoreImagePageView> pageList;

    public StorePhotoPagerAdapter(ArrayList<StoreImagePageView> pageList) {
        super();
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        Log.d( "Plzzzzz"," pagList.size : " +pageList.size());
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageList.get(position));
        return pageList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        notifyDataSetChanged();
    }
}
