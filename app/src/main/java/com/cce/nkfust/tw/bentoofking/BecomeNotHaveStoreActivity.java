package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class BecomeNotHaveStoreActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private Context context;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Button nextButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText time1EditText;
    private EditText time2EditText;
    private EditText time3EditText;
    private EditText time4EditText;
    private TextView weekTextView;
    private TextView infoContentTextView;
    private String storeInfoString = "0000000";
    private int hour=0, minute=0;
    private char[] bussinessTimeChar;
    private Store store;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private char[] week = {'一','二','三','四','五','六','日'};
    private boolean[] storeWeekStringBool = {false,false,false,false,false,false,false};
    private String weekStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        bussinessTimeChar = new char[16];
        for(int i=0;i<bussinessTimeChar.length;i++)bussinessTimeChar[i]='0';
        setContentView(R.layout.activity_become_not_have_store);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store = (Store) intent.getSerializableExtra(passStoreInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.addStore));
        nextButton = findViewById(R.id.completeButton);
        NextHandler nextHandler = new NextHandler();
        nextButton.setOnClickListener(nextHandler);
        infoContentTextView = findViewById(R.id.infoContentTextView);
        InfoClickHandler infoClickHandler = new InfoClickHandler();
        infoContentTextView.setOnClickListener(infoClickHandler);
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        time1EditText = findViewById(R.id.time1EditText);
        time2EditText = findViewById(R.id.time2EditText);
        time3EditText = findViewById(R.id.time3EditText);
        time4EditText = findViewById(R.id.time4EditText);
        weekTextView = findViewById(R.id.weekTextView);
        weekStr = new String();
        time1EditText.setInputType(InputType.TYPE_NULL);
        time2EditText.setInputType(InputType.TYPE_NULL);
        time3EditText.setInputType(InputType.TYPE_NULL);
        time4EditText.setInputType(InputType.TYPE_NULL);
        TimeTextFieldOnFocusHandler timeTextFieldOnFocusHandler = new TimeTextFieldOnFocusHandler();
        time1EditText.setOnFocusChangeListener(timeTextFieldOnFocusHandler);
        time2EditText.setOnFocusChangeListener(timeTextFieldOnFocusHandler);
        time3EditText.setOnFocusChangeListener(timeTextFieldOnFocusHandler);
        time4EditText.setOnFocusChangeListener(timeTextFieldOnFocusHandler);
        TimeTextFieldOnClickHandler timeTextFieldOnClickHandler = new TimeTextFieldOnClickHandler();
        time1EditText.setOnClickListener(timeTextFieldOnClickHandler);
        time2EditText.setOnClickListener(timeTextFieldOnClickHandler);
        time3EditText.setOnClickListener(timeTextFieldOnClickHandler);
        time4EditText.setOnClickListener(timeTextFieldOnClickHandler);
        weekTextView.setOnClickListener(new weekClickHandler());
    }
    public class weekClickHandler implements View.OnClickListener{
        CharSequence[] weekInfo;
        AlertDialog alertDialog;

        @Override
        public void onClick(View view) {
            weekInfo = getResources().getStringArray(R.array.weekInfo);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMultiChoiceItems(weekInfo,storeWeekStringBool,new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        storeWeekStringBool[which] = true;

                    }else{
                        storeWeekStringBool[which] = false;
                    }
                }});
            builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    weekStr = "";
                    for(int i=0;i<7;i++) {
                        if (storeWeekStringBool[i]==true) {
                            if(!weekStr.equals("")) weekStr += "、";
                            weekStr += week[i];
                        }
                    }
                    if(weekStr.equals("")) weekStr = getResources().getString(R.string.touchToEdit);
                    weekTextView.setText(weekStr);
                    dialog.dismiss();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }
    public class InfoClickHandler implements View.OnClickListener{
        CharSequence[] storeInfo;
        AlertDialog alertDialog;
        char[] storeInfoStringTmp = storeInfoString.toCharArray();
        boolean[] storeInfoStringBool = new boolean[storeInfoStringTmp.length];
        String infoContentTextViewString;

        @Override
        public void onClick(View view) {
            for(int i=0;i<storeInfoStringTmp.length;i++){
                if(storeInfoStringTmp[i]=='0') storeInfoStringBool[i]=false;
                else storeInfoStringBool[i]=true;
            }
            storeInfo = getResources().getStringArray(R.array.storeInfo);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMultiChoiceItems(storeInfo,storeInfoStringBool,new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        storeInfoStringTmp[which] = '1';

                    }else{
                        storeInfoStringTmp[which] = '0';
                    }
                    }});
            builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    infoContentTextViewString = "";
                    storeInfoString = new String(storeInfoStringTmp);
                    int length=0;
                    for(int i=0;i<7;i++) {
                        if (storeInfoStringTmp[i] == '1') {
                            if (length != 0) infoContentTextViewString += "、";
                            if (length == 3) infoContentTextViewString += "\n";
                            infoContentTextViewString += getResources().getStringArray(R.array.storeInfo)[i];
                            length++;

                        }
                    }
                    if(infoContentTextViewString.equals("")) infoContentTextViewString = getResources().getString(R.string.touchToEdit);
                    infoContentTextView.setText(infoContentTextViewString);
                    dialog.dismiss();
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public class TimeTextFieldOnFocusHandler implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
                switch (view.getId()){
                    case R.id.time1EditText:
                        timeInput(1);
                        break;
                    case R.id.time2EditText:
                        timeInput(2);
                        break;
                    case R.id.time3EditText:
                        timeInput(3);
                        break;
                    case R.id.time4EditText:
                        timeInput(4);
                        break;
                }
            }
        }
    }
    public class TimeTextFieldOnClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.time1EditText:
                    timeInput(1);
                    break;
                case R.id.time2EditText:
                    timeInput(2);
                    break;
                case R.id.time3EditText:
                    timeInput(3);
                    break;
                case R.id.time4EditText:
                    timeInput(4);
                    break;
            }
        }
    }

    public void timeInput(int i){
        final int type = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v = View.inflate(context, R.layout.time_picker, null);
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        TimePickerHandler timePickerHandler = new TimePickerHandler();
        timePicker.setOnTimeChangedListener(timePickerHandler);
        builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type){
                    case 1:
                        bussinessTimeChar[1] = (char)('0'+(hour%10));
                        bussinessTimeChar[0] = (char)('0'+(hour/10));
                        bussinessTimeChar[3] = (char)('0'+(minute%10));
                        bussinessTimeChar[2] = (char)('0'+(minute/10));
                        time1EditText.setText(""+bussinessTimeChar[0]+bussinessTimeChar[1]+":"+bussinessTimeChar[2]+bussinessTimeChar[3]);
                        break;
                    case 2:
                        bussinessTimeChar[5] = (char)('0'+(hour%10));
                        bussinessTimeChar[4] = (char)('0'+(hour/10));
                        bussinessTimeChar[7] = (char)('0'+(minute%10));
                        bussinessTimeChar[6] = (char)('0'+(minute/10));
                        time2EditText.setText(""+bussinessTimeChar[4]+bussinessTimeChar[5]+":"+bussinessTimeChar[6]+bussinessTimeChar[7]);
                        break;
                    case 3:
                        bussinessTimeChar[9] = (char)('0'+(hour%10));
                        bussinessTimeChar[8] = (char)('0'+(hour/10));
                        bussinessTimeChar[11] = (char)('0'+(minute%10));
                        bussinessTimeChar[10] = (char)('0'+(minute/10));
                        time3EditText.setText(""+bussinessTimeChar[8]+bussinessTimeChar[9]+":"+bussinessTimeChar[10]+bussinessTimeChar[11]);

                        break;
                    case 4:
                        bussinessTimeChar[13] = (char)('0'+(hour%10));
                        bussinessTimeChar[12] = (char)('0'+(hour/10));
                        bussinessTimeChar[15] = (char)('0'+(minute%10));
                        bussinessTimeChar[14] = (char)('0'+(minute/10));
                        time4EditText.setText(""+bussinessTimeChar[12]+bussinessTimeChar[13]+":"+bussinessTimeChar[14]+bussinessTimeChar[15]);
                        break;
                }

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

    public class NextHandler implements View.OnClickListener{


        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(BecomeNotHaveStoreActivity.this, "請稍等...", "資料上傳中...", true);

            if(nameEditText.getText().toString().equals("")){
                Toast toast = Toast.makeText(context,
                        getResources().getString(R.string.nameError), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
                return;
            }
            if(phoneEditText.getText().toString().equals("")){
                Toast toast = Toast.makeText(context,
                        getResources().getString(R.string.phoneError), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
                return;
            }
            if(time1EditText.getText().toString().equals("") || time2EditText.getText().toString().equals("")){
                Toast toast = Toast.makeText(context,
                        getResources().getString(R.string.businessHoursError), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
                return;
            }
            String bussinessTime = new String(bussinessTimeChar);
            for(int i=0;i<7;i++){
                if(storeWeekStringBool[i] == true){
                    bussinessTime += "1";
                }else{
                    bussinessTime += "0";
                }
            }
            store.putStoreName(nameEditText.getText().toString());
            store.putAddress(addressEditText.getText().toString());
            store.putPhone(phoneEditText.getText().toString());
            store.putBusinessHours(bussinessTime);
            store.putInformation(storeInfoString);
            Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addressLocation = geoCoder.getFromLocationName(addressEditText.getText().toString(), 1);
                store.putLatitude(Double.toString(addressLocation.get(0).getLatitude()));
                store.putLongitude(Double.toString(addressLocation.get(0).getLongitude()));
                progressDialog.dismiss();
            } catch (Exception e) {
                Toast toast = Toast.makeText(context,
                        getResources().getString(R.string.addressError), Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
                progressDialog.dismiss();
                return;
            }
            Intent intent = new Intent();
            intent.setClass(context,EditMenuActivity.class);
            intent.putExtra(passUserInfo,userInfo);
            intent.putExtra(passStoreInfo,store);
            context.startActivity(intent);

        }
    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }


}
