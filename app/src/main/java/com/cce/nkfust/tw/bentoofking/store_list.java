package com.cce.nkfust.tw.bentoofking;

import android.graphics.Bitmap;
import android.text.format.Time;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Asus on 2018/4/13.
 */

public class store_list {


    private String imageURL;
    private String storename;
    private String evaluation;
    private String price;
    private String distance;
    private String status;
    private Store store;
    private Bitmap bitmap = null;
    public store_list(Store inputStore){
        store = inputStore;
        this.imageURL = store.getFirstPhoto();
        this.storename=store.getStoreName();
        this.evaluation=store.getRank();
        this.price=store.getPrice();
        this.distance=store.getDistance();
        setDoBusiness(inputStore.getBusinessHours(),inputStore.getTemporaryRest());
    }

    private void setDoBusiness(String storeTime,String dayOff){
        if(!dayOff.equals("")) {
            ArrayList<String> dayOffArray = transformDayOffString(dayOff);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date(System.currentTimeMillis());
            for(int i = 0; i < dayOffArray.size(); i++){
                if(dayOffArray.get(i).equals(simpleDateFormat.format(date))){
                    this.status = "未營業";
                    return;
                }
            }
        }
        Time time = new Time();
        time.setToNow();
        int timeNow = time.hour*60 + time.minute;
        int amStart=0,amEnd=0,pmStart=0,pmEnd=0;
        if(storeTime.length()>=8) {
            amStart = Integer.valueOf(storeTime.substring(0, 2)) * 60 + Integer.valueOf(storeTime.substring(2, 4));
            amEnd = Integer.valueOf(storeTime.substring(4, 6)) * 60 + Integer.valueOf(storeTime.substring(6, 8));
        }
        if(storeTime.length()>=16) {
            pmStart = Integer.valueOf(storeTime.substring(8, 10)) * 60 + Integer.valueOf(storeTime.substring(10, 12));
            pmEnd = Integer.valueOf(storeTime.substring(12, 14)) * 60 + Integer.valueOf(storeTime.substring(14, 16));
        }
        if((timeNow>=amStart&&timeNow<amEnd)||(timeNow>=pmStart&&timeNow<pmEnd)) {
            Calendar cal = Calendar.getInstance();
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            switch(dayOfWeek){
                case Calendar.MONDAY:
                    if(storeTime.charAt(16) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                    break;
                case Calendar.TUESDAY:
                    if(storeTime.charAt(17) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if(storeTime.charAt(18) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                    break;
                case Calendar.THURSDAY:
                    if(storeTime.charAt(19) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                    break;
                case Calendar.FRIDAY:
                    if(storeTime.charAt(20) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                    break;
                case Calendar.SATURDAY:
                    if(storeTime.charAt(21) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                case Calendar.SUNDAY:
                    if(storeTime.charAt(22) == '0'){
                        this.status = "未營業";
                    }else{
                        this.status = "營業中";
                    }
                    break;
            }
        }else {
            this.status = "未營業";
        }
    }
    private ArrayList<String> transformDayOffString(String transformString) {
        String remainString = transformString;
        ArrayList<String> dayOffArray = new ArrayList<String>();
        if(transformString.indexOf(",")<0)
            dayOffArray.add(transformString);
        else {
            while(remainString.indexOf(",")>=0) {
                dayOffArray.add(remainString.substring(0,remainString.indexOf(",")));
                remainString = remainString.substring(remainString.indexOf(",")+1);
            }
            dayOffArray.add(remainString);
        }
        return dayOffArray;
    }
    public String getStorename(){
        return this.storename;
    }
    public void setStorename(String storename){
        this.storename=storename;
    }
    public String getEvaluation(){
        return this.evaluation;
    }
    public void setEvaluation(String evaluation){ this.evaluation=evaluation; }
    public String getPrice(){
        return this.price;
    }
    public void setPrice(String price){
        this.price=price;
    }
    public String getDistance(){
        return this.distance;
    }
    public void setDistance(String distance){ this.distance=distance; }
    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public String getImageURL(){ return this.imageURL; }
    public void setImageURL(String imageURL){ this.imageURL = imageURL; }
    public Store getStoreInfo(){ return this.store; }
    public void setStoreBitmap(Bitmap bitmap){ this.bitmap = bitmap; }
    public Bitmap getStoreBitmap(){ return this.bitmap; }

}
