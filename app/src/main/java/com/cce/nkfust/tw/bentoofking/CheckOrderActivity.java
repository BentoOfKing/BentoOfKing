package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckOrderActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passOrderID = "ORDER_ID";
    private UserInfo userInfo;
    private String orderID;
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Database database;
    private ArrayList<OrderIncludeMeal> orderIncludeMeals;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private TextView costTextView;
    private OrderMealAdapter orderMealAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_order);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        orderID = (String) intent.getSerializableExtra(passOrderID);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        costTextView = findViewById(R.id.costTextView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.findOrders));
        mealListView = findViewById(R.id.mealListView);
        orderIncludeMeals = new ArrayList<OrderIncludeMeal>();
        database = new Database();
        mainThreadHandler = new MainThreadHandler();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //orderMealAdapter = new OrderMealAdapter(inflater,orderIncludeMeals);
        mealListView.setAdapter(orderMealAdapter);
        Thread t = new Thread(new GetMeal());
        t.start();
    }

    public class GetMeal implements Runnable{
        @Override
        public void run() {
            try {
                ArrayList<OrderIncludeMeal> tmp = database.GetOrderMeal(orderID);
                for(int i=0;i<tmp.size();i++) {
                    orderIncludeMeals.add(tmp.get(i));
                }
                mainThreadHandler.sendEmptyMessage(SUCCESS);
            }catch (Exception e){
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }

    public class MainThreadHandler extends Handler {
        public MainThreadHandler() {
            super();
        }
        public MainThreadHandler(Looper looper) {
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    orderMealAdapter.notifyDataSetChanged();
                    int total = 0;
                    for(int i=0;i<orderIncludeMeals.size();i++){
                        int price = Integer.parseInt(orderIncludeMeals.get(i).getMeal().getPrice());
                        int count = Integer.parseInt(orderIncludeMeals.get(i).getCount());
                        total += price*count;
                    }
                    costTextView.setText("一共花了 "+total+" 元");
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
