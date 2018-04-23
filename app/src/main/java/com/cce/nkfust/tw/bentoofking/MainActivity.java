package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int DATABASE_CONNECTED = 5278;
    private Handler mainHandler;
    private HandlerThread CDBThread;
    private Handler CDBTHandler;
    private UserInfo userInfo;
    private long mExitTime;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Database database;
    private Store store[];
    private ListView storelist;
    private ArrayList<store_list> storeLists = new ArrayList<store_list>();
    private StoreListViewBaseAdapter adapter;
    private Button locationButton;
    private Button sortButton;
    private Button filterButton;
    private CharSequence[] countryList;
    private int locationState = 0,distanceState = 1 ,rankState = 0,priceState = 0,distanceKm = 25;
    private boolean bussinessState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        creatHandler();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if(userInfo == null) userInfo = new UserInfo();
        toolbar = findViewById(R.id.toolbar);
        countryList = getResources().getStringArray(R.array.country);
        locationButton = findViewById(R.id.locationButton);
        sortButton = findViewById(R.id.sortButton);
        filterButton = findViewById(R.id.filterButton);
        ConditionButtonHandler conditionButtonHandler = new ConditionButtonHandler();
        locationButton.setOnClickListener(conditionButtonHandler);
        sortButton.setOnClickListener(conditionButtonHandler);
        filterButton.setOnClickListener(conditionButtonHandler);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        storelist=(ListView)findViewById(R.id.storeListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        ConnectDatabase connectDatabase = new ConnectDatabase();
        CDBTHandler.post(connectDatabase);
        //GetCoordinate getCoordinate = new GetCoordinate();
        //getCoordinate.requestUserLocation(this);

        /*
        Thread thread = new Thread(connectDatabase);
        thread.start();


        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    }
    public class ConditionButtonHandler implements View.OnClickListener{
        int locationStateTmp = locationState;
        Spinner distanceSpinner;
        Spinner rankSpinner;
        Spinner priceSpinner;
        LayoutInflater inflater;
        View view;
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        TextView distanceTextView;
        int distanceKmTmp = distanceKm;
        CheckBox bussinessCheckBox;

        String[] sortState={getResources().getStringArray(R.array.distanceSetting)[distanceState],getResources().getStringArray(R.array.rankSetting)[rankState],getResources().getStringArray(R.array.priceSetting)[priceState]};
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.locationButton:
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getResources().getString(R.string.chooseCountry))
                            .setSingleChoiceItems(countryList, locationState,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            locationStateTmp = which;
                                        }
                                    })
                            .setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    locationState = locationStateTmp;
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                case R.id.sortButton:
                    inflater = LayoutInflater.from(MainActivity.this);
                    View view = inflater.inflate(R.layout.alertdialog_sort,null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getResources().getString(R.string.chooseSort));
                    builder.setView(view);
                    distanceSpinner = view.findViewById(R.id.distanceSpinner);
                    rankSpinner = view.findViewById(R.id.rankSpinner);
                    priceSpinner = view.findViewById(R.id.priceSpinner);
                    ArrayAdapter<CharSequence> distanceList = ArrayAdapter.createFromResource(MainActivity.this, R.array.distanceSetting,android.R.layout.simple_spinner_dropdown_item);
                    distanceSpinner.setAdapter(distanceList);
                    distanceSpinner.setSelection(distanceState);
                    ArrayAdapter<CharSequence> rankList = ArrayAdapter.createFromResource(MainActivity.this, R.array.rankSetting,android.R.layout.simple_spinner_dropdown_item);
                    rankSpinner.setAdapter(rankList);
                    rankSpinner.setSelection(rankState);
                    ArrayAdapter<CharSequence> priceList = ArrayAdapter.createFromResource(MainActivity.this, R.array.priceSetting,android.R.layout.simple_spinner_dropdown_item);
                    priceSpinner.setAdapter(priceList);
                    priceSpinner.setSelection(priceState);

                    builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            distanceState = (int)distanceSpinner.getSelectedItemId();
                            rankState = (int)rankSpinner.getSelectedItemId();
                            priceState = (int)priceSpinner.getSelectedItemId();
                            dialog.dismiss();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case R.id.filterButton:
                    inflater = LayoutInflater.from(MainActivity.this);
                    view = inflater.inflate(R.layout.alertdialog_filter,null);
                    builder = new AlertDialog.Builder(MainActivity.this);
                    SeekBar distanceSeekBar = view.findViewById(R.id.distanceSeekBar);
                    distanceTextView = view.findViewById(R.id.distanceTextView);
                    distanceTextView.setText(getResources().getString(R.string.distance) + "：" + distanceKm + getResources().getString(R.string.km));
                    bussinessCheckBox = view.findViewById(R.id.bussinessCheckBox);
                    bussinessCheckBox.setChecked(bussinessState);
                    distanceSeekBar.setProgress(distanceKm);
                    SeekBar.OnSeekBarChangeListener seekBarOnSeekBarChange
                            = new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar){}
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar){}

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                        {
                            distanceKmTmp = progress;
                            distanceTextView.setText(getResources().getString(R.string.distance) + "：" + Integer.toString(distanceKmTmp) + getResources().getString(R.string.km));
                        }
                    };
                    distanceSeekBar.setOnSeekBarChangeListener(seekBarOnSeekBarChange);
                    builder.setTitle(getResources().getString(R.string.chooseFilter));
                    builder.setView(view);
                    builder.setNegativeButton(getResources().getString(R.string.cancel),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.check),new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            distanceKm = distanceKmTmp;
                            if(bussinessCheckBox.isChecked()){
                                bussinessState = true;
                            }else{
                                bussinessState = false;
                            }
                            dialog.dismiss();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
            }
        }
    }
    public class ConnectDatabase implements Runnable{
        @Override
        public void run() {
            database = new Database();
            if(database.GetStoreInit()) store = database.GetStore();
            for(int i=0;i<store.length;i++){
                storeLists.add(new store_list(store[i].getStoreName()," * * * * *",100+i+" ","10KM",store[i].getState(),store[i].getPhoto()));
            }
            mainHandler.sendEmptyMessage(DATABASE_CONNECTED);
        }
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this,"再按一次離開程式", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
    }
    private void creatHandler(){
        mainHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch(msg.what){
                    case DATABASE_CONNECTED:
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        adapter = new StoreListViewBaseAdapter(MainActivity.this,storeLists,inflater);
                        storelist.setAdapter(adapter);
                        StoreListClickHandler storeListClickHandler = new StoreListClickHandler();
                        storelist.setOnItemClickListener(storeListClickHandler);
                        break;
                }
            }
        };
        CDBThread = new HandlerThread("ConnectDataBase");
        CDBThread.start();
        CDBTHandler = new Handler(CDBThread.getLooper());
    }



    private class StoreListClickHandler implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this,CheckStoreInfo.class);
            UserInfo storeInfoBundle = new UserInfo();
            storeInfoBundle.setIdentity(2);
            storeInfoBundle.putStore(MainActivity.this.store[position]);
            intent.putExtra("storeInfo",storeInfoBundle);
            intent.putExtra( MainActivity.passUserInfo, MainActivity.this.userInfo);
            startActivity(intent);
        }
    }





}
