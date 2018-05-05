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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Store store;
    private ArrayList<Meal> meal;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    OrderMealAdapter adapter;
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
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.order));
        mealListView = findViewById(R.id.mealListView);
        meal = new ArrayList<Meal>();
        GetMeal getMeal = new GetMeal();
        mainThreadHandler = new MainThreadHandler(Looper.getMainLooper());
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
                    adapter = new OrderMealAdapter(context, meal);
                    mealListView.setAdapter(adapter);
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }

    }

}
