package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderActivity extends AppCompatActivity {
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static String passOrderInfo = "ORDER_INFO";
    private ArrayList<String> mealIDs;
    private ArrayList<byte[]> mealBitmaps;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Store store;
    private ArrayList<MealClass> mealClass;
    private ArrayList<OrderMenuItem> orderMenuItem,passOrder,inputOrder;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private Button orderButton,clearButton;
    private TextView orderStatisticsTextView;
    private OrderMealAdapter adapter;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        try {
            inputOrder = (ArrayList<OrderMenuItem>) intent.getSerializableExtra(passOrderInfo);
        }catch(Exception e){
            inputOrder = null;
        }
        mealIDs = (ArrayList<String>) intent.getExtras().getSerializable("special_meal_ID");
        mealBitmaps = (ArrayList<byte[]>) intent.getExtras().getSerializable("special_meal_bitmap");
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        orderButton = findViewById(R.id.orderButton);
        clearButton = findViewById(R.id.clearButton);
        orderStatisticsTextView = findViewById(R.id.orderStatisticsTextView);
        orderButton.setOnClickListener(new OrderButtonHandler());
        clearButton.setOnClickListener(new ClearButtonHandler());
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.order));
        mealListView = findViewById(R.id.mealListView);
        mealClass = new ArrayList<MealClass>();
        GetMeal getMeal = new GetMeal();
        mainThreadHandler = new MainThreadHandler(Looper.getMainLooper());
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料載入中...", true);
        Thread thread = new Thread(getMeal);
        thread.start();

    }

    class GetMeal implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                mealClass = database.getMeal(store.getID());
                mainThreadHandler.sendEmptyMessage(SUCCESS);
            }catch (Exception e){
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }
    public class MainThreadHandler extends Handler {
        public MainThreadHandler(){
            super();
        }
        public MainThreadHandler(Looper looper){
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    orderMenuItem = new ArrayList<OrderMenuItem>();
                    for(int i=0;i<mealClass.size();i++){
                        for(int j=0;j<mealClass.size();j++){
                            if(Integer.toString(i).equals(mealClass.get(j).getSequence())){
                                orderMenuItem.add(new OrderMenuItem(mealClass.get(j).getName(),"-1","0","",""));
                                for(int ii=0;ii<mealClass.get(j).getMeal().size();ii++){
                                    for(int jj=0;jj<mealClass.get(j).getMeal().size();jj++){
                                        if(Integer.toString(ii).equals(mealClass.get(j).getMeal().get(jj).getSequence())){
                                            orderMenuItem.add(new OrderMenuItem(mealClass.get(j).getMeal().get(jj).getName(),mealClass.get(j).getMeal().get(jj).getPrice(),"0",mealClass.get(j).getMeal().get(jj).getID(),mealClass.get(j).getMeal().get(jj).getDescription()));
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    int totalCount=0,totalPrice=0;
                    if(inputOrder!=null){
                        for(int i=0;i<orderMenuItem.size();i++){
                            for(int j=0;j<inputOrder.size();j++){
                                if(orderMenuItem.get(i).getID().equals(inputOrder.get(j).getID())){
                                    if(!inputOrder.get(j).getCount().equals("-1")) {
                                        totalCount += Integer.parseInt(inputOrder.get(j).getCount());
                                        totalPrice += Integer.parseInt(inputOrder.get(j).getCount()) * Integer.parseInt(inputOrder.get(j).getPrice());
                                        orderMenuItem.get(i).putCount(inputOrder.get(j).getCount());
                                    }
                                    inputOrder.remove(j);
                                }
                            }
                        }
                    }
                    orderStatisticsTextView.setText("共 "+totalCount+" 個便當，"+totalPrice+" 元");
                    adapter = new OrderMealAdapter(inflater, orderMenuItem);
                    mealListView.setAdapter(adapter);
                    mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        LayoutInflater inflater;
                        NumberPicker numberPicker;
                        TextView descriptionTextView;
                        AlertDialog alertDialog;
                        @Override
                        public void onItemClick(AdapterView arg0, View arg1, int arg2,final long arg3) {
                            int nowIndex = (int)arg3;
                            if(orderMenuItem.get((int)arg3).getPrice().equals("-1")){
                                orderMenuItem.get((int)arg3).putPrice("-2");
                                nowIndex++;
                                while(nowIndex<orderMenuItem.size()){
                                    if(orderMenuItem.get(nowIndex).getPrice().equals("-1")|| orderMenuItem.get(nowIndex).getPrice().equals("-2"))
                                        break;
                                    orderMenuItem.get(nowIndex).putState("0");
                                    nowIndex++;
                                }
                                adapter.notifyDataSetChanged();
                                return;
                            }
                            if(orderMenuItem.get((int)arg3).getPrice().equals("-2")){
                                orderMenuItem.get((int)arg3).putPrice("-1");
                                nowIndex++;
                                while(nowIndex<orderMenuItem.size()){
                                    if(orderMenuItem.get(nowIndex).getPrice().equals("-1")|| orderMenuItem.get(nowIndex).getPrice().equals("-2"))
                                        break;
                                    orderMenuItem.get(nowIndex).putState("1");
                                    nowIndex++;
                                }
                                adapter.notifyDataSetChanged();
                                return;
                            }
                            inflater = LayoutInflater.from(OrderActivity.this);
                            View view = inflater.inflate(R.layout.alertdialog_order,null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                            builder.setTitle(orderMenuItem.get(nowIndex).getName());
                            builder.setView(view);
                            descriptionTextView = view.findViewById(R.id.descriptionTextView);
                            if(orderMenuItem.get(nowIndex).getDescription().equals("")){
                                descriptionTextView.setVisibility(View.GONE);
                            }else{
                                descriptionTextView.setText(orderMenuItem.get(nowIndex).getDescription());
                            }
                            ImageView mealPhoto = view.findViewById(R.id.mealPhoto);
                            mealPhoto.setVisibility(View.GONE);
                            for(int i = 0;i<mealIDs.size();i++){
                                if(mealIDs.get(i).equals(orderMenuItem.get((int)arg3).getID())){
                                    mealPhoto.setImageBitmap(BitmapFactory.decodeByteArray(mealBitmaps.get(i), 0, mealBitmaps.get(i).length));
                                    mealPhoto.setVisibility(View.VISIBLE);
                                }
                            }
                            numberPicker = view.findViewById(R.id.numberPicker);
                            numberPicker.setMinValue(0);
                            numberPicker.setMaxValue(1000);
                            numberPicker.setValue(Integer.parseInt(orderMenuItem.get((int)arg3).getCount()));
                            builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    orderMenuItem.get((int)arg3).putCount(Integer.toString(numberPicker.getValue()));
                                    adapter.notifyDataSetChanged();
                                    int totalCount = 0,totalPrice = 0;
                                    for(int i=0;i<orderMenuItem.size();i++){
                                        if(!orderMenuItem.get(i).getPrice().equals("-1")) {
                                            totalCount += Integer.parseInt(orderMenuItem.get(i).getCount());
                                        }
                                        totalPrice += Integer.parseInt(orderMenuItem.get(i).getCount())*Integer.parseInt(orderMenuItem.get(i).getPrice());
                                    }
                                    orderStatisticsTextView.setText("共 "+totalCount+" 個便當，"+totalPrice+" 元");
                                    dialog.dismiss();

                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();


                        }
                    });
                    progressDialog.dismiss();
                    break;
                case FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);

        }

    }

    public class OrderButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            passOrder = new ArrayList<OrderMenuItem>();
            for(int i=0;i<orderMenuItem.size();i++){
                if(!orderMenuItem.get(i).getCount().equals("0")){
                    OrderMenuItem thisItem = orderMenuItem.get(i);
                    thisItem.putState("2");
                    passOrder.add(thisItem);
                }
            }
            Intent intent = new Intent();
            intent.setClass(context,OrderFinalActivity.class);
            intent.putExtra(passUserInfo,userInfo);
            intent.putExtra(passStoreInfo,store);
            intent.putExtra(passOrderInfo,passOrder);
            startActivity(intent);

        }
    }


    public class ClearButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            for(int i=0;i<orderMenuItem.size();i++){
                orderMenuItem.get(i).putCount("0");
            }
            orderStatisticsTextView.setText("共 0 個便當，0 元");
            adapter.notifyDataSetChanged();

        }
    }

}
