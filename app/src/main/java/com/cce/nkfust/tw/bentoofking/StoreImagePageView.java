package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StoreImagePageView extends ConstraintLayout {
    private Bitmap storeImageBitMap;
    private ImageView storePicture;
    private TextView photoMark;
    public StoreImagePageView( Context context, Bitmap bitmap, int currentWidth, String photoMark){
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_2_check_storeinfo_viewpager_item,null);
        storePicture = (ImageView)view.findViewById(R.id.storePicture);
        this.photoMark = (TextView)view.findViewById(R.id.pictureMark);
        storeImageBitMap = bitmap;
        storePicture.setImageBitmap(storeImageBitMap);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) storePicture.getLayoutParams();
        params.width = currentWidth;
        storePicture.setLayoutParams(params);
        storePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(photoMark.charAt(0) == '*')
            this.photoMark.setText(photoMark.substring(1));
        addView(view);

    }


}
