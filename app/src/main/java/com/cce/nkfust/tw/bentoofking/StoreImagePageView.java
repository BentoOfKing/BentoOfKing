package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class StoreImagePageView extends ConstraintLayout {
    private Bitmap storeImageBitMap;
    private ImageView storePicture;
    public StoreImagePageView( Context context, Bitmap bitmap, int currentWidth){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_2_check_storeinfo_viewpager_item,null);
        storePicture = (ImageView)view.findViewById(R.id.storePicture);
        storeImageBitMap = bitmap;
        storePicture.setImageBitmap(storeImageBitMap);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) storePicture.getLayoutParams();
        params.width = currentWidth;
        storePicture.setLayoutParams(params);
        storePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(view);

    }


}
