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
import android.view.*;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckOrderActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passOrderID = "ORDER_ID";
    private static String passStoreInfo = "STORE_INFO";
    private static String passOrderInfo = "ORDER_INFO";
    private UserInfo userInfo;
    private String orderID;
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Database database;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private TextView costTextView;
    private OrderMealAdapter orderMealAdapter;
    private ArrayList<OrderMenuItem> orderMenuItem;
    private Button checkButton;
    private Button copyButton;
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
        costTextView = findViewById(R.id.stateTextView);
        Drawer drawer = new Drawer();

        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.findOrders));

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deleteItem:
                        class DeleteOrder implements Runnable{
                            @Override
                            public void run() {
                                database.DeleteOrder(orderID);
                            }
                        }
                        Thread DeleteT = new Thread(new DeleteOrder());
                        DeleteT.start();
                        Toast.makeText(context,"訂單已刪除", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
                return false;
            }
        });
        drawer.setToolbarNavigation();
        mealListView = findViewById(R.id.mealListView);
        database = new Database();
        checkButton = findViewById(R.id.checkButton);
        copyButton = findViewById(R.id.copyButton);
        if(orderID.charAt(orderID.length()-1) == '*'){
            orderID = orderID.substring(0,orderID.length()-1);
            checkButton.setVisibility(View.VISIBLE);
        }else{
            checkButton.setVisibility(View.GONE);
        }
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                class CheckOrder implements Runnable{
                    @Override
                    public void run() {
                        database.CheckOrder(orderID);
                    }
                }
                Thread CheckT = new Thread(new CheckOrder());
                CheckT.start();
                Toast.makeText(context, "訂單已確認", Toast.LENGTH_SHORT).show();
                checkButton.setVisibility(View.GONE);
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CheckOrderActivity.this,OrderActivity.class);
                intent.putExtra(passStoreInfo,userInfo.getStore());
                intent.putExtra(passUserInfo,userInfo);
                intent.putExtra(passOrderInfo,orderMenuItem);
                startActivity(intent);
            }
        });
        mainThreadHandler = new MainThreadHandler();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        orderMenuItem = new ArrayList<OrderMenuItem>();
        orderMealAdapter = new OrderMealAdapter(inflater,orderMenuItem);
        mealListView.setAdapter(orderMealAdapter);
        Thread t = new Thread(new GetMeal());
        t.start();
    }

    public class GetMeal implements Runnable{
        @Override
        public void run() {
            try {
                ArrayList<OrderMenuItem> tmp = database.GetOrderMeal(orderID);
                Store store = new Store();
                store.putID(tmp.get(0).getID());
                store.putPhone(tmp.get(0).getName());
                store.putState(tmp.get(0).getPrice());
                userInfo.putStore(store);
                for(int i=1;i<tmp.size();i++) {
                    orderMenuItem.add(tmp.get(i));
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
                    for(int i=0;i<orderMenuItem.size();i++){
                        int price = Integer.parseInt(orderMenuItem.get(i).getPrice());
                        int count = Integer.parseInt(orderMenuItem.get(i).getCount());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
