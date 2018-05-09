package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v4.view.NestedScrollingParent;
import android.widget.ScrollView;

import java.util.jar.Attributes;

public class StoreNestedScrollParent extends NestedScrollView implements NestedScrollingParent {

    private NestedScrollingParentHelper storeParentHelper;
    private int imgHeight = 180;
    private RecyclerView mscroller;

    private GestureDetectorCompat gestureDetectorCompat;

    public StoreNestedScrollParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        storeParentHelper = new NestedScrollingParentHelper(this);
        setNestedScrollingEnabled(true);

    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final ImageView imageView = findViewById(R.id.storePicture);
        mscroller = findViewById(R.id.commentRecyclerView);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgHeight = imageView.getHeight();
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
        //return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL)!=0;
    }


    @Override
    public  void onNestedScrollAccepted (View child, View target, int nestedScrollAxes ) {
        storeParentHelper. onNestedScrollAccepted ( child, target, nestedScrollAxes ) ;
    }

    @Override
    public void onStopNestedScroll(View child) {
        storeParentHelper.onStopNestedScroll(child);
    }

    @Override
    public  void onNestedPreScroll (View target, int dx, int dy , int [ ] consumed ) {


        boolean hiddenTop = dy > 0 && getScrollY() < imgHeight&& ViewCompat.canScrollVertically(target,-1);
        boolean showTop = dy < 0 && getScrollY() > 0 && ViewCompat.canScrollVertically(target,1);
        if(hiddenTop||showTop) {
            consumed[0] = 0;
            consumed[1] = dy;
        }else{
            consumed[0] = 0;
            consumed[1] = 0;
        }
        super.onNestedPreScroll(target,dx,dy,consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override public  void scrollTo (int x, int y )  {
        if (y < 0)
            y = 0;
        if (y > imgHeight)
            y = imgHeight;
        if (y != getScrollY())
            super.scrollTo(x, y);
    }


    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return getScrollY() < imgHeight;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);/*
        ViewGroup.LayoutParams layoutParams = mscroller.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        mscroller.setLayoutParams(layoutParams);
        setMeasuredDimension(getMeasuredWidth(),imgHeight+mscroller.getMeasuredHeight()+500);
*/
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }


}
