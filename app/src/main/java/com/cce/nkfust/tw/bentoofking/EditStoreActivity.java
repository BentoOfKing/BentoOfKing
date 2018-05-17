package com.cce.nkfust.tw.bentoofking;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.HandlerThread;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class EditStoreActivity extends AppCompatActivity {

    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private Context context;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ImageButton nextButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText NewPasswordEditText;
    private EditText passwordCheckEditText;
    private EditText time1EditText;
    private EditText time2EditText;
    private EditText time3EditText;
    private EditText time4EditText;
    private TextView infoContentTextView;
    private String storeInfoString = "0000000";
    private int hour=0, minute=0;
    private char[] bussinessTimeChar;
    private Store store;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    private Handler EditStoreThreadHandler;
    private HandlerThread EditStoreThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        bussinessTimeChar = new char[16];
        for(int i=0;i<bussinessTimeChar.length;i++)bussinessTimeChar[i]='0';
        setContentView(R.layout.activity_edit_store);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store =  userInfo.getStore();
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.editStore));
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
        passwordEditText = findViewById(R.id.passwordEditText);
        NewPasswordEditText = findViewById(R.id.NewPasswordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
        time2EditText = findViewById(R.id.time2EditText);
        time3EditText = findViewById(R.id.time3EditText);
        time4EditText = findViewById(R.id.time4EditText);
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

        nameEditText.setText(store.getStoreName().toString());
        addressEditText.setText(store.getAddress().toString());
        phoneEditText.setText(store.getPhone().toString());


        String timetemp = store.BusinessHours.toString();
        char chartime ;
        String intime1="";
        String test1="";
        String intime2="";
        String test2="";
        String intime3="";
        String test3="";
        String intime4="";
        String test4="";
        int i;
            for (i = 0; i <= 15; i++) {
                if (i >= 0 && i <= 3) {
                    chartime = timetemp.charAt(i);
                    intime1 = intime1 + chartime;
                    test1 = test1 + chartime;
                    if(i==1){intime1=intime1+':';}
                    }
                if (i >= 4 && i <= 7) {
                    chartime = timetemp.charAt(i);
                    intime2 = intime2 + chartime;
                    test2 = test2 + chartime;
                    if(i==5){intime2=intime2+':';}
                }
                if (i >= 8 && i <= 11) {
                    chartime = timetemp.charAt(i);
                    intime3 = intime3 + chartime;
                    test3 = test3 + chartime;
                    if(i==9){intime3=intime3+':';}
                }
                if (i >= 12 && i <= 15) {
                    chartime = timetemp.charAt(i);
                    intime4 = intime4 + chartime;
                    test4 = test4 + chartime;
                    if(i==13){intime4=intime4+':';}
                }
            }
            if(test1.equals("0000")) intime1="";
                else time1EditText.setText(intime1);
            if(test2.equals("0000")) intime2="";
                else time2EditText.setText(intime2);
            if(test3.equals("0000")) intime3="";
                else time3EditText.setText(intime3);
            if(test4.equals("0000")) intime4="";
                else time4EditText.setText(intime4);


        char charinfo;
        String infotemp =store.Information.toString() ;
        String infoContentTextViewString="";
        int length = 0;
        for(int j=0;j<7;j++) {
            charinfo = infotemp.charAt(j);
            if(charinfo=='1') {
                if (length != 0) infoContentTextViewString += "、";
                if (length == 3) infoContentTextViewString += "\n";
                infoContentTextViewString += getResources().getStringArray(R.array.storeInfo)[j];
                length++;
            }
        }
        if(infoContentTextViewString.equals("")) infoContentTextViewString = getResources().getString(R.string.touchToEdit);
        infoContentTextView.setText(infoContentTextViewString);





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
                    store.putInformation(storeInfoString);//
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





            EditStoreThread = new HandlerThread("Login2");
            EditConfirm editConfirm = new EditConfirm();
            EditStoreThread.start();
            EditStoreThreadHandler = new Handler(EditStoreThread.getLooper());
            EditStoreThreadHandler.post(editConfirm);
            progressDialog = ProgressDialog.show(EditStoreActivity.this, "請稍等...", "資料上傳中...", true);

        }

        public class EditConfirm implements Runnable{
            @Override
            public void run() {

////////////////////////////////////////////////////////////////////////////


                if (passwordEditText.getText().toString().equals(store.Password)) {
                    int passwordFlag=0;
                    if(NewPasswordEditText.getText().toString().equals("")&&passwordCheckEditText.getText().toString().equals("")){}

                    else {
                        if (NewPasswordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())) {
                            store.putPassword(NewPasswordEditText.getText().toString());
                        } else {
                            Toast toast = Toast.makeText(EditStoreActivity.this,
                                    getResources().getString(R.string.NewPasswordAndCheckPassword), Toast.LENGTH_LONG);
                            toast.show();
                            passwordFlag=1;
                        }
                    }
                if(passwordFlag==0) {
                    String time1 = time1EditText.getText().toString();
                    String time2 = time2EditText.getText().toString();
                    String time3 = time3EditText.getText().toString();
                    String time4 = time4EditText.getText().toString();
                    String worktime = "";
                    char chartime;
                    int i, j;
                    for (i = 0; i <= 3; i++) {
                        if (i == 0) {
                            if (time1.equals("")) {
                                worktime = worktime + "0000";
                            } else {
                                for (j = 0; j < 5; j++) {
                                    chartime = time1.charAt(j);
                                    if (chartime != ':') {
                                        worktime = worktime + chartime;
                                    }
                                }
                            }
                        }

                        if (i == 1) {
                            if (time2.equals("")) {
                                worktime = worktime + "0000";
                            } else {
                                for (j = 0; j < 5; j++) {
                                    chartime = time2.charAt(j);
                                    if (chartime != ':') {
                                        worktime = worktime + chartime;
                                    }
                                }
                            }
                        }

                        if (i == 2) {
                            if (time3.equals("")) {
                                worktime = worktime + "0000";
                            } else {
                                for (j = 0; j < 5; j++) {
                                    chartime = time3.charAt(j);
                                    if (chartime != ':') {
                                        worktime = worktime + chartime;
                                    }
                                }
                            }
                        }

                        if (i == 3) {
                            if (time4.equals("")) {
                                worktime = worktime + "0000";
                            } else {
                                for (j = 0; j < 5; j++) {
                                    chartime = time4.charAt(j);
                                    if (chartime != ':') {
                                        worktime = worktime + chartime;
                                    }
                                }
                            }
                        }
                    }


                    storeInfoString = store.getInformation();


////////////////////////////////////////////////////////


                    if (nameEditText.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(context,
                                getResources().getString(R.string.nameError), Toast.LENGTH_LONG);
                        toast.show();
                        progressDialog.dismiss();
                        return;
                    }
                    if (phoneEditText.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(context,
                                getResources().getString(R.string.phoneError), Toast.LENGTH_LONG);
                        toast.show();
                        progressDialog.dismiss();
                        return;
                    }
                    if (time1EditText.getText().toString().equals("") || time2EditText.getText().toString().equals("")) {
                        Toast toast = Toast.makeText(context,
                                getResources().getString(R.string.businessHoursError), Toast.LENGTH_LONG);
                        toast.show();
                        progressDialog.dismiss();
                        return;
                    }
                    store.putStoreName(nameEditText.getText().toString());
                    store.putAddress(addressEditText.getText().toString());
                    store.putPhone(phoneEditText.getText().toString());
                    store.putBusinessHours(worktime);
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
                }//passwordFlag
                    else{
                    Database database = new Database();
                    database.UpdateStore(store);
                    progressDialog.dismiss();

                }

                    Handler EditUiHandler = new Handler();  //修改
                    DatabaseLogin databaseLogin = new DatabaseLogin();
                    EditUiHandler.post(databaseLogin);



                }//if
                else{
                    Database database = new Database();
                    database.UpdateStore(store);
                    progressDialog.dismiss();
                    Toast toast = Toast.makeText(EditStoreActivity.this,
                            getResources().getString(R.string.PleaseCheckPassword), Toast.LENGTH_LONG);
                    toast.show();


                }
            }//run
        }


        public class DatabaseLogin implements Runnable{
            @Override
            public void run() {
                if (NewPasswordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())) {
                    Database database = new Database();
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    intent.putExtra(passUserInfo, userInfo);
                    intent.putExtra(passStoreInfo, store);
                    context.startActivity(intent);
                    database.UpdateStore(store);

                    Toast toast = Toast.makeText(EditStoreActivity.this,
                            getResources().getString(R.string.EditSuccessful), Toast.LENGTH_LONG);
                    toast.show();
                }
                else{
                    Database database = new Database();
                    database.UpdateStore(store);

                }
            }
        }









    }


    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

}
