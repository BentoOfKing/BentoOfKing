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
    public static final int REQUEST_CALL_PHONE = 9;
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Store store;
    private ArrayList<Meal> meal;
    private ArrayList<OrderIncludeMeal> order,passOrder;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private Button orderButton,clearButton;
    private TextView orderStatisticsTextView;
    private OrderMealAdapter adapter;
    private MemberOrder memberOrder;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
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
        meal = new ArrayList<Meal>();
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
                meal = database.getMeal(store.getID());
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
                    order = new ArrayList<OrderIncludeMeal>();
                    for(int i=0;i<meal.size();i++) {
                        for (int j = 0; j < meal.size(); j++) {
                            if (meal.get(j).getSequence().equals(Integer.toString(i))) {
                                order.add(new OrderIncludeMeal("",meal.get(j),"0"));
                            }
                        }
                    }
                    adapter = new OrderMealAdapter(inflater, order);
                    mealListView.setAdapter(adapter);
                    mealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        LayoutInflater inflater;
                        NumberPicker numberPicker;
                        AlertDialog alertDialog;
                        @Override
                        public void onItemClick(AdapterView arg0, View arg1, int arg2,final long arg3) {
                            inflater = LayoutInflater.from(OrderActivity.this);
                            View view = inflater.inflate(R.layout.alertdialog_order,null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                            builder.setTitle(getResources().getString(R.string.pickNumber));
                            builder.setView(view);
                            numberPicker = view.findViewById(R.id.numberPicker);
                            numberPicker.setMinValue(0);
                            numberPicker.setMaxValue(1000);
                            numberPicker.setValue(Integer.parseInt(order.get((int)arg3).getCount()));
                            builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    order.get((int)arg3).putCount(Integer.toString(numberPicker.getValue()));
                                    adapter.notifyDataSetChanged();
                                    int totalCount = 0,totalPrice = 0;
                                    for(int i=0;i<order.size();i++){
                                        totalCount += Integer.parseInt(order.get(i).getCount());
                                        totalPrice += Integer.parseInt(order.get(i).getCount())*Integer.parseInt(order.get(i).getMeal().getPrice());
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
            getCallPermission();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
            Date curDate = new Date(System.currentTimeMillis()) ;
            String str = formatter.format(curDate);
            memberOrder = new MemberOrder();
            memberOrder.putMember(userInfo.getMember().getEmail());
            memberOrder.putTime(str);
            passOrder = new ArrayList<OrderIncludeMeal>();
            for(int i=0;i<order.size();i++){
                if(!order.get(i).getCount().equals("0")){
                    passOrder.add(order.get(i));
                }
            }
            Thread thread = new Thread(new Order());
            thread.start();
        }
    }

    class Order implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                String ID = database.AddOrder(memberOrder);
                for(int i=0;i<passOrder.size();i++){
                    passOrder.get(i).putOrderID(ID);
                }
                database.AddOrderMeal(passOrder);
            }catch (Exception e){

            }
        }
    }
    public class ClearButtonHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            for(int i=0;i<order.size();i++){
                order.get(i).putCount("0");
            }
            orderStatisticsTextView.setText("共 0 個便當，0 元");
            adapter.notifyDataSetChanged();
        }
    }
    public void getCallPermission(){
        int permission = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.CALL_PHONE},REQUEST_CALL_PHONE);
            return ;
        }else{
            new AlertDialog.Builder(context)
                    .setTitle(R.string.hint)
                    .setMessage(R.string.callHint)
                    .setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store.getPhone()));
                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                                startActivity(myIntentDial);
                        }
                    })
                    .show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CALL_PHONE:
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, getResources().getString(R.string.errorPermission), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
