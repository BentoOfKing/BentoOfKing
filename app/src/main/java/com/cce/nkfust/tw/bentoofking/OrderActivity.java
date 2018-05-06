package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<OrderIncludeMeal> order;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private Button orderButton;
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
        orderButton = findViewById(R.id.orderButton);
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
                                    dialog.dismiss();

                                }
                            });
                            alertDialog = builder.create();
                            alertDialog.show();

                        }
                    });
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);

        }

    }

    public class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
            Date curDate = new Date(System.currentTimeMillis()) ;
            String str = formatter.format(curDate);
            MemberOrder memberOrder = new MemberOrder();
            memberOrder.putMember(userInfo.getMember().getEmail());
            memberOrder.putTime(str);
            for(int i=0;i<meal.size();i++){

            }

        }
    }
}
