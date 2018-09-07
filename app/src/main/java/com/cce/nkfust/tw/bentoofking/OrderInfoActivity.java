package com.cce.nkfust.tw.bentoofking;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderInfoActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passOrderInfo = "ORDER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static String passMemberOrderInfo = "MEMBER_ORDER_INFO";
    public static final int REQUEST_CALL_PHONE = 9;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private Store store;
    private Button orderButton;
    private ArrayList<OrderMenuItem> passOrder;
    private ProgressDialog progressDialog;
    private Spinner countrySpinner,citySpinner;
    private String[] cityAllString;
    private String[] cityString;
    private ArrayAdapter<String> cityAdapter;
    private int countrySelected = 0,citySelected = 0;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText nameEditText;
    private EditText timeEditText;
    private Button checkButton;
    private int hour=0, minute=0;
    private SharedPreferences setting;
    private MemberOrder memberOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =(Store) intent.getSerializableExtra(passStoreInfo);
        passOrder = (ArrayList<OrderMenuItem>) intent.getSerializableExtra(passOrderInfo);
        memberOrder = (MemberOrder)intent.getSerializableExtra(passMemberOrderInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle("訂單資訊");
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        nameEditText = findViewById(R.id.nameEditText);
        timeEditText = findViewById(R.id.timeEditText);
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new ButtonHandler());
        cityAllString = getResources().getStringArray(R.array.city);
        countrySpinner = findViewById(R.id.countrySpinner);
        citySpinner = findViewById(R.id.citySpinner);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this, R.array.countryForAddress, android.R.layout.simple_spinner_item );
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeEditText.setCursorVisible(false);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityAdapter = null;
                countrySelected = (int)l;
                if(l == 0) {
                    cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item , getResources().getStringArray(R.array.cityDefault));
                }else {
                    cityString = cityAllString[(int) l - 1].split(",");
                    cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cityString);

                }
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);
                if(!setting.getString("userCity","").equals("")){
                    citySpinner.setSelection(Integer.parseInt(setting.getString("userCity","")));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                citySelected = (int) l;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    timeEditText.setInputType(InputType.TYPE_NULL);
                    pickTime();
                }
            }
        });
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeEditText.setInputType(InputType.TYPE_NULL);
                pickTime();
            }
        });
        setting = getSharedPreferences("atm", MODE_PRIVATE);
        if(!setting.getString("userCountry","").equals("")){
            countrySpinner.setSelection(Integer.parseInt(setting.getString("userCountry","")));
        }
        if(!setting.getString("userCity","").equals("")){
            countrySpinner.setSelection(Integer.parseInt(setting.getString("userCountry","")));
        }
        addressEditText.setText(setting.getString("userAddress", ""));
        phoneEditText.setText(setting.getString("userPhone", ""));
        nameEditText.setText(setting.getString("userName", ""));
        if(!setting.getString("userHour","").equals("") && !setting.getString("userMinute","").equals("")){
            hour = Integer.parseInt(setting.getString("userHour",""));
            minute = Integer.parseInt(setting.getString("userMinute",""));
            timeEditText.setText(hour+"："+minute);
        }
    }

    public void pickTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.time_picker, null);
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        TimePickerHandler timePickerHandler = new TimePickerHandler();
        timePicker.setOnTimeChangedListener(timePickerHandler);
        builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timeEditText.setText(hour+"："+minute);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle(getResources().getString(R.string.chooseTime));
        builder.setView(v);
        builder.show();
    }

    public class TimePickerHandler implements TimePicker.OnTimeChangedListener{

        @Override
        public void onTimeChanged(TimePicker timePicker, int h, int m) {
            hour = h;
            minute = m;
        }
    }
    public class ButtonHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String address = addressEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String sendTime = timeEditText.getText().toString();
            if(countrySelected == 0){
                Toast toast = Toast.makeText(context,"請選擇縣市", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(address.equals("")){
                Toast toast = Toast.makeText(context,"請輸入地址", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(phone.equals("")){
                Toast toast = Toast.makeText(context,"請輸入連絡電話", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(name.equals("")){
                Toast toast = Toast.makeText(context,"請輸入聯絡人", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if(sendTime.equals("")){
                Toast toast = Toast.makeText(context,"請輸入送達時間", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            setting = getSharedPreferences("atm", MODE_PRIVATE);
            setting.edit().putString("userCountry", Integer.toString(countrySelected)).commit();
            setting.edit().putString("userCity", Integer.toString(citySelected)).commit();
            setting.edit().putString("userAddress", address).commit();
            setting.edit().putString("userPhone", phone).commit();
            setting.edit().putString("userName", name).commit();
            setting.edit().putString("userHour", Integer.toString(hour)).commit();
            setting.edit().putString("userMinute", Integer.toString(minute)).commit();
            address = getResources().getStringArray(R.array.countryForAddress)[countrySelected]+cityString[citySelected]+address;
            memberOrder.putAddress(address);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
            Date curDate = new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            memberOrder.putTime(str);
            memberOrder.putMember(userInfo.getMember().getEmail());
            memberOrder.putStore(store.getID());
            memberOrder.putState("0");
            memberOrder.putPhone(phone);
            memberOrder.putName(name);
            memberOrder.putSendTime(sendTime);
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
    class Order implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                String ID = database.AddOrder(memberOrder);
                database.AddOrderMeal(ID,passOrder);
                memberOrder.putID(ID);
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
            NotificationManager manager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= 26){
                String id = "channel_1";
                String description = "123";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(id, "123", importance);
                manager.createNotificationChannel(mChannel);
                Notification notification = new Notification.Builder(context, id).setContentTitle("Title")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("便當王訂單")
                        .setContentText("訂單編號："+store.getID()+"-"+memberOrder.getTime()+"-"+memberOrder.getID())
                        .setAutoCancel(true)
                        .build();
                manager.notify(1, notification);
            }else{
                NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"OrderInfo");
                Notification notification =new NotificationCompat.Builder(this,"default")
                        .setContentTitle("便當王訂單")
                        .setContentText("訂單編號："+store.getID()+"-"+memberOrder.getTime()+"-"+memberOrder.getID())
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .build();
                manager.notify(1,notification);
            }

                new AlertDialog.Builder(context)
                        .setTitle(R.string.hint)
                        .setMessage("您的訂單編號為 "+store.getID()+"-"+memberOrder.getTime()+"-"+memberOrder.getID()+"，訂單已發送給店家，直接告知店家訂單編號即可！")
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
