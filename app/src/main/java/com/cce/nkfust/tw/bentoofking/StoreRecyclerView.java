package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class StoreRecyclerView extends RecyclerView {

    public StoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean startNestedScroll(int axes) {
        return true;
    }
}
