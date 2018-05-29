package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderFinalActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passOrderInfo = "ORDER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    public static final int REQUEST_CALL_PHONE = 9;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private Store store;
    private Button orderButton;
    private ArrayList<OrderIncludeMeal> orders;
    private MemberOrder memberOrder;
    private OrderMealAdapter adapter;
    private TextView orderStatisticsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_final);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        orders = (ArrayList<OrderIncludeMeal>) intent.getSerializableExtra(passOrderInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle("確認餐點");
        orderButton = findViewById(R.id.orderButton);
        mealListView = findViewById(R.id.mealListView);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new OrderMealAdapter(inflater,orders);
        mealListView.setAdapter(adapter);
        orderButton.setOnClickListener(new OrderButtonHandler());
        orderStatisticsTextView = findViewById(R.id.orderStatisticsTextView);
        int count=0,total=0;
        for(int i=0;i<orders.size();i++){
            count += Integer.parseInt(orders.get(i).getCount());
            total += Integer.parseInt(orders.get(i).getCount())*Integer.parseInt(orders.get(i).getMeal().getPrice());
        }
        orderStatisticsTextView.setText("共 "+count+" 個便當，"+total+" 元");


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
                for(int i=0;i<orders.size();i++){
                    orders.get(i).putOrderID(ID);
                }
                database.AddOrderMeal(orders);
            }catch (Exception e){

            }
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
