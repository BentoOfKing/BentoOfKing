package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
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
    private static String passMemberOrderInfo = "MEMBER_ORDER_INFO";
    public static final int REQUEST_CALL_PHONE = 9;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,mealListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private Store store;
    private Button orderButton;
    private ArrayList<OrderMenuItem> passOrder;
    private MemberOrder memberOrder;
    private OrderMealAdapter adapter;
    private TextView orderStatisticsTextView;
    private ProgressDialog progressDialog;
    private int count=0,total=0;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_final);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        passOrder = (ArrayList<OrderMenuItem>) intent.getSerializableExtra(passOrderInfo);
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
        adapter = new OrderMealAdapter(inflater,passOrder);
        mealListView.setAdapter(adapter);
        orderButton.setOnClickListener(new OrderAddressCheckHandler());
        orderStatisticsTextView = findViewById(R.id.orderStatisticsTextView);
        for(int i=0;i<passOrder.size();i++){
            count += Integer.parseInt(passOrder.get(i).getCount());
            total += Integer.parseInt(passOrder.get(i).getCount())*Integer.parseInt(passOrder.get(i).getPrice());
        }
        orderStatisticsTextView.setText("共 "+count+" 個便當，"+total+" 元");


    }

    public class OrderAddressCheckHandler implements View.OnClickListener{
        LayoutInflater inflater;
        AlertDialog alertDialog;
        EditText addressEditText;
        @Override
        public void onClick(View v) {
            if(!store.getEmail().equals("")) {
                memberOrder = new MemberOrder();
                memberOrder.putPrice(Integer.toString(total));
                Intent intent = new Intent();
                intent.setClass(context,OrderInfoActivity.class);
                intent.putExtra(passUserInfo,userInfo);
                intent.putExtra(passStoreInfo,store);
                intent.putExtra(passOrderInfo,passOrder);
                intent.putExtra(passMemberOrderInfo,memberOrder);
                context.startActivity(intent);
                /*inflater = LayoutInflater.from(OrderFinalActivity.this);
                View view = inflater.inflate(R.layout.alertdialog_order_address, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderFinalActivity.this);
                builder.setTitle("請輸入送餐地址");
                builder.setView(view);
                addressEditText = view.findViewById(R.id.addressEditText);
                SharedPreferences setting = getSharedPreferences("atm", MODE_PRIVATE);
                addressEditText.setText(setting.getString("userAddress",""));
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (addressEditText.getText().toString().equals("")) {
                            Toast.makeText(context, "請輸入地址！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        memberOrder = new MemberOrder();
                        memberOrder.putAddress(addressEditText.getText().toString());
                        SharedPreferences setting = getSharedPreferences("atm", MODE_PRIVATE);
                        setting.edit()
                                .putString("userAddress", memberOrder.getAddress())
                                .commit();
                        dialog.dismiss();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
                        Date curDate = new Date(System.currentTimeMillis());
                        String str = formatter.format(curDate);
                        memberOrder.putMember(userInfo.getMember().getEmail());
                        memberOrder.putTime(str);
                        memberOrder.putStore(store.getID());
                        memberOrder.putPrice(Integer.toString(total));
                        if (store.getState().equals("1")) {
                            memberOrder.putState("0");
                        } else {
                            memberOrder.putState("4");
                        }
                        Thread thread = new Thread(new Order());
                        thread.start();
                        progressDialog = ProgressDialog.show(context, "請稍等...", "訂單傳送中...", true);
                        try {
                            thread.join();
                            getCallPermission();
                            progressDialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(context, "訂單傳送失敗，請確認網路狀態！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();*/
            }else{
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                memberOrder = new MemberOrder();
                memberOrder.putMember(userInfo.getMember().getEmail());
                memberOrder.putTime(str);
                memberOrder.putStore(store.getID());
                memberOrder.putPrice(Integer.toString(total));
                memberOrder.putAddress("");
                memberOrder.putState("4");
                Thread thread = new Thread(new Order());
                thread.start();
                progressDialog = ProgressDialog.show(context, "請稍等...", "訂單傳送中...", true);
                try {
                    thread.join();
                    getCallPermission();
                    progressDialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, "訂單傳送失敗，請確認網路狀態！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class Order implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                ID = database.AddOrder(memberOrder);
                database.AddOrderMeal(ID,passOrder);
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
            if(!store.getEmail().equals("")){
                new AlertDialog.Builder(context)
                        .setTitle(R.string.hint)
                        .setMessage("您的訂單編號為 "+ID+"，訂單已發送給店家，直接告知店家訂單編號即可！")
                        .setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + store.getPhone()));
                                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                                    startActivity(myIntentDial);
                            }
                        })
                        .show();
                 }else {
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
