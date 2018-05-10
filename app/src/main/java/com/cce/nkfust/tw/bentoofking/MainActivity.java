package com.cce.nkfust.tw.bentoofking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int REFRESH_ACTIVITY = 5278, requestCodeFineLoaction = 1, requestCodeCoarseLocation = 2, MORE_STORE = 5279, REFRESH_STORELIST = 5273, SEND_FILTER_REFRESH = 5274, SEND_LAST_FILTER = 5275, SEND_GPS_FILTER = 5276, REFRESHING = 5277;
    private static final int GET_USERINFO = 6667,CREATE_DRAWER = 6669;
    private MainThreadHandler mainHandler;
    private HandlerThread CDBThread;
    private Handler_A CDBTHandler;
    private UserInfo userInfo;
    private long mExitTime;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Database database;
    private Store store[];
    private ListView storelist;
    private ArrayList<store_list> storeLists;
    private StoreListViewBaseAdapter adapter;
    private Button locationButton;
    private Button sortButton;
    private Button filterButton;
    private SwipeRefreshLayout swipeLayout;
    private CharSequence[] countryList;
    private int locationState = 15, distanceState = 0, rankState = 1, priceState = 0, distanceKm = 25;
    private boolean bussinessState = false;
    private String Longitude, Latitude;
    private LocationManager status;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocation;
    private Boolean userLocationNotChanged = true;
    private Boolean isGPSenabled = false;
    private Boolean isNetworkEnabled = false;
    private Boolean returnBool = false;
    private Member RecordMember;
    private Store RecordStore;
    private Admin RecordAdmin;
    private ProgressDialog progressDialog;
    private Context context;
    private Handler LoginRecordThreadHandler;
    private HandlerThread LoginRecordThread;
    private String searchString = "%";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InfoReceive();
        varialbleSetup();
        UIconnect();
        UIsetup();
        UIhandle();
        UIupdate();
    }

    private void varialbleSetup() {
        countryList = getResources().getStringArray(R.array.country);
        database = new Database();
        mainHandler = new MainThreadHandler(Looper.getMainLooper());
        CDBThread = new HandlerThread("1stThread");
        CDBThread.start();
        CDBTHandler = new Handler_A(CDBThread.getLooper());
        storeLists = new ArrayList<store_list>();
        adapter = new StoreListViewBaseAdapter(MainActivity.this, storeLists, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        MainActivity.this.Longitude = "22.736802";
        MainActivity.this.Latitude = "120.331109";
    }

    private void InfoReceive() {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if (userInfo == null) userInfo = new UserInfo();
    }

    private void UIconnect() {
        context = this;
        toolbar = findViewById(R.id.toolbar);
        locationButton = findViewById(R.id.locationButton);
        sortButton = findViewById(R.id.sortButton);
        filterButton = findViewById(R.id.filterButton);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        storelist = findViewById(R.id.storeListView);
        swipeLayout = findViewById(R.id.swipeLayout);
    }

    private void UIhandle() {
        ConditionButtonHandler conditionButtonHandler = new ConditionButtonHandler();
        locationButton.setOnClickListener(conditionButtonHandler);
        sortButton.setOnClickListener(conditionButtonHandler);
        filterButton.setOnClickListener(conditionButtonHandler);
        swipeLayout.setOnRefreshListener(new StoreRefreshListener());
        storelist.setOnItemClickListener(new StoreListClickHandler());
        storelist.setOnScrollListener(new StoreListScrollHandler());
    }

    private void UIsetup() {
        swipeLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.mapItem:
                        Intent intent = new Intent();
                        intent.setClass(context,HomeMapsActivity.class);
                        intent.putExtra(passUserInfo,userInfo);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        Drawer drawer = new Drawer();
        drawer.init(this, toolbar, drawerListView, drawerLayout, userInfo);
        storelist.setAdapter(adapter);
        CDBTHandler.sendEmptyMessage(GET_USERINFO);
    }

    private void UIupdate() {
        CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);

    }


    public class ConditionButtonHandler implements View.OnClickListener {
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

        String[] sortState = {getResources().getStringArray(R.array.distanceSetting)[distanceState], getResources().getStringArray(R.array.rankSetting)[rankState], getResources().getStringArray(R.array.priceSetting)[priceState]};
        int locationStateTmp = locationState;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //缺更新資料
                                    locationState = locationStateTmp;
                                    CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    break;
                case R.id.sortButton:
                    inflater = LayoutInflater.from(MainActivity.this);
                    View view = inflater.inflate(R.layout.alertdialog_sort, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(getResources().getString(R.string.chooseSort));
                    builder.setView(view);
                    distanceSpinner = view.findViewById(R.id.distanceSpinner);
                    rankSpinner = view.findViewById(R.id.rankSpinner);
                    priceSpinner = view.findViewById(R.id.priceSpinner);
                    ArrayAdapter<CharSequence> distanceList = ArrayAdapter.createFromResource(MainActivity.this, R.array.distanceSetting, android.R.layout.simple_spinner_dropdown_item);
                    distanceSpinner.setAdapter(distanceList);
                    distanceSpinner.setSelection(distanceState);
                    if (locationState != 0) {
                        distanceSpinner.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                    }
                    ArrayAdapter<CharSequence> rankList = ArrayAdapter.createFromResource(MainActivity.this, R.array.rankSetting, android.R.layout.simple_spinner_dropdown_item);
                    rankSpinner.setAdapter(rankList);
                    rankSpinner.setSelection(rankState);
                    ArrayAdapter<CharSequence> priceList = ArrayAdapter.createFromResource(MainActivity.this, R.array.priceSetting, android.R.layout.simple_spinner_dropdown_item);
                    priceSpinner.setAdapter(priceList);
                    priceSpinner.setSelection(priceState);

                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //缺更新資料
                            distanceState = (int) distanceSpinner.getSelectedItemId();
                            rankState = (int) rankSpinner.getSelectedItemId();
                            priceState = (int) priceSpinner.getSelectedItemId();
                            CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);
                            dialog.dismiss();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case R.id.filterButton:
                    inflater = LayoutInflater.from(MainActivity.this);
                    view = inflater.inflate(R.layout.alertdialog_filter, null);
                    builder = new AlertDialog.Builder(MainActivity.this);
                    SeekBar distanceSeekBar = view.findViewById(R.id.distanceSeekBar);
                    distanceTextView = view.findViewById(R.id.distanceTextView);
                    bussinessCheckBox = view.findViewById(R.id.bussinessCheckBox);
                    bussinessCheckBox.setChecked(bussinessState);
                    if (locationState == 0) {
                        distanceTextView.setText(getResources().getString(R.string.distance) + "：" + distanceKm + getResources().getString(R.string.km));
                        distanceSeekBar.setProgress(distanceKm);
                        SeekBar.OnSeekBarChangeListener seekBarOnSeekBarChange
                                = new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                distanceKmTmp = progress;
                                distanceTextView.setText(getResources().getString(R.string.distance) + "：" + Integer.toString(distanceKmTmp) + getResources().getString(R.string.km));
                            }
                        };
                        distanceSeekBar.setOnSeekBarChangeListener(seekBarOnSeekBarChange);
                    } else {
                        distanceTextView.setText(getResources().getString(R.string.distance) + "：" + getResources().getString(R.string.canNotUse));
                        distanceSeekBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                    }
                    builder.setTitle(getResources().getString(R.string.chooseFilter));
                    builder.setView(view);
                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //缺更新資料
                            distanceKm = distanceKmTmp;
                            bussinessState = bussinessCheckBox.isChecked();
                            CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);
                            dialog.dismiss();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                    break;
            }
        }
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次離開程式", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
    }


    private class Handler_A extends Handler {
        public Handler_A(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MORE_STORE:
                    if (msg.getData().getInt("locationStateNow") != 0)
                        store = database.GetStore(MainActivity.this.searchString,getResources().getStringArray(R.array.country)[msg.getData().getInt("locationStateNow")], msg.getData().getInt("rankStateNow"), msg.getData().getInt("priceStateNow")); //,msg.getData().getInt
                    else
                        store = database.GetStoreByPosition(MainActivity.this.searchString,msg.getData().getString("LongitudeNow"), msg.getData().getString("LatitudeNow"), msg.getData().getInt("distanceStateNow"), msg.getData().getInt("rankStateNow"), msg.getData().getInt("priceStateNow"), msg.getData().getInt("distanceNow"));
                    for (int i = 0; i < store.length; i++) {
                        store_list additem = new store_list(store[i]);
                        if ((!msg.getData().getBoolean("businessStateNow")) || additem.getStatus().equals("營業中"))
                            storeLists.add(new store_list(store[i]));
                    }
                    mainHandler.sendEmptyMessage(REFRESH_ACTIVITY);
                    break;
                case REFRESH_STORELIST:
                    database.refreshStoreIndex();
                    if (msg.getData().getInt("locationStateNow") != 0)
                        store = database.GetStore(MainActivity.this.searchString,getResources().getStringArray(R.array.country)[msg.getData().getInt("locationStateNow")], msg.getData().getInt("rankStateNow"), msg.getData().getInt("priceStateNow"));
                    else
                        store = database.GetStoreByPosition(MainActivity.this.searchString,msg.getData().getString("LongitudeNow"), msg.getData().getString("LatitudeNow"), msg.getData().getInt("distanceStateNow"), msg.getData().getInt("rankStateNow"), msg.getData().getInt("priceStateNow"), msg.getData().getInt("distanceNow"));
                    storeLists.clear();
                    for (int i = 0; i < store.length; i++) {
                        store_list additem = new store_list(store[i]);
                        if ((!msg.getData().getBoolean("businessStateNow")) || additem.getStatus().equals("營業中"))
                            storeLists.add(new store_list(store[i]));
                    }
                    mainHandler.sendEmptyMessage(REFRESH_ACTIVITY);
                    break;
                case SEND_FILTER_REFRESH:
                    if (!swipeLayout.isRefreshing()) mainHandler.sendEmptyMessage(REFRESHING);
                    Message filter = new Message();
                    filter.what = REFRESH_STORELIST;
                    Bundle bag = new Bundle();
                    if (MainActivity.this.locationState == 0) {
                        requestUserLocation();
                        break;
                    }
                    bag.putBoolean("businessStateNow", MainActivity.this.bussinessState);
                    bag.putInt("locationStateNow", MainActivity.this.locationState);
                    bag.putInt("rankStateNow", MainActivity.this.rankState);
                    bag.putInt("priceStateNow", MainActivity.this.priceState);
                    filter.setData(bag);
                    CDBTHandler.sendMessage(filter);
                    break;
                case SEND_LAST_FILTER:
                    if (!swipeLayout.isRefreshing()) mainHandler.sendEmptyMessage(REFRESHING);
                    Message lastFilter = new Message();
                    lastFilter.what = MORE_STORE;
                    Bundle lastBag = new Bundle();
                    if (MainActivity.this.locationState == 0) {
                        lastBag.putString("LongitudeNow", MainActivity.this.Longitude);
                        lastBag.putString("LatitudeNow", MainActivity.this.Latitude);
                        lastBag.putInt("distanceStateNow", MainActivity.this.distanceState);
                        lastBag.putInt("distanceNow", MainActivity.this.distanceKm);
                    }
                    lastBag.putInt("locationStateNow", MainActivity.this.locationState);
                    lastBag.putInt("rankStateNow", MainActivity.this.rankState);
                    lastBag.putInt("priceStateNow", MainActivity.this.priceState);
                    lastFilter.setData(lastBag);
                    CDBTHandler.sendMessage(lastFilter);
                    break;
                case SEND_GPS_FILTER:
                    if (!swipeLayout.isRefreshing()) mainHandler.sendEmptyMessage(REFRESHING);
                    Message GPSfilter = new Message();
                    GPSfilter.what = REFRESH_STORELIST;
                    Bundle GPSbag = new Bundle();
                    GPSbag.putString("LongitudeNow", MainActivity.this.Longitude);
                    GPSbag.putString("LatitudeNow", MainActivity.this.Latitude);
                    GPSbag.putInt("distanceStateNow", MainActivity.this.distanceState);
                    GPSbag.putInt("distanceNow", MainActivity.this.distanceKm);
                    GPSbag.putBoolean("businessStateNow", MainActivity.this.bussinessState);
                    GPSbag.putInt("locationStateNow", MainActivity.this.locationState);
                    GPSbag.putInt("rankStateNow", MainActivity.this.rankState);
                    GPSbag.putInt("priceStateNow", MainActivity.this.priceState);
                    GPSfilter.setData(GPSbag);
                    CDBTHandler.sendMessage(GPSfilter);
                    break;
                case GET_USERINFO:
                    SharedPreferences LoginRecord = getApplication().
                            getSharedPreferences("LoginRecord", Context.MODE_PRIVATE);
                    String NumberTemp = LoginRecord.getString("Recordemail","*");
                    String PasswordTemp = LoginRecord.getString("Recordpassword","*");
                    int RecordFlag = LoginRecord.getInt("RecordFlag",0);
                    database = new Database();

                    if(userInfo.getIdentity()==4){
                        LoginRecord.edit()
                                .putString("Recordemail","*")
                                .putString("Recordpassword","*")
                                .putInt("RecordFlag",0)
                                .commit();
                         NumberTemp = LoginRecord.getString("Recordemail","*");
                         PasswordTemp = LoginRecord.getString("Recordpassword","*");
                        RecordFlag = LoginRecord.getInt("RecordFlag",0);
                        userInfo.getIdentity();

                    }


                    if(RecordFlag==1) {
                        RecordMember = database.MemberLogin(NumberTemp, PasswordTemp);
                        userInfo.putMember(RecordMember);
                        userInfo.setIdentity(1);
                    }
                    else if(RecordFlag==2) {
                        RecordStore = database.StoreLogin(NumberTemp, PasswordTemp);
                        userInfo.putStore(RecordStore);
                        userInfo.setIdentity(2);
                    }
                    else if(RecordFlag==3) {
                        RecordAdmin = database.AdminLogin(NumberTemp, PasswordTemp);
                        userInfo.putAdmin(RecordAdmin);
                        userInfo.setIdentity(3);
                    }
                    mainHandler.sendEmptyMessage(CREATE_DRAWER);
                    break;
            }
        }
    }

    private class MainThreadHandler extends Handler {
        public MainThreadHandler() {
            super();
        }

        public MainThreadHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_ACTIVITY:
                    adapter.notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                    storelist.setEnabled(true);
                    break;
                case REFRESHING:
                    swipeLayout.setRefreshing(true);
                    break;
                case CREATE_DRAWER:
                    Drawer drawer = new Drawer();
                    drawer.init(MainActivity.this, toolbar, drawerListView, drawerLayout, userInfo);
                    break;
            }
            super.handleMessage(msg);
        }
    }


    private class StoreListClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, CheckStoreInfo.class);
            UserInfo storeInfoBundle = new UserInfo();
            storeInfoBundle.setIdentity(2);
            storeInfoBundle.putStore(MainActivity.this.storeLists.get(position).getStoreInfo());
            intent.putExtra("storeInfo", storeInfoBundle);
            intent.putExtra(MainActivity.passUserInfo, MainActivity.this.userInfo);
            startActivity(intent);
        }
    }


    private class StoreListScrollHandler implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    CDBTHandler.sendEmptyMessage(SEND_LAST_FILTER);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    //---以下為定位程式---
    public boolean requestUserLocationPermission() {
        int permissionFineLoaction = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFineLoaction != PackageManager.PERMISSION_GRANTED || permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeFineLoaction);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, requestCodeCoarseLocation);
            return false;
        } else return true;
    }

    public void requestUserLocation() {
        //判斷當前是否已經獲得了定位權限
        int permissionFineLoaction = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionFineLoaction != PackageManager.PERMISSION_GRANTED || permissionCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            boolean isGPSEnabled = mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGPSEnabled) {
                setupGPS();
                isGPSEnabled = mLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            boolean isNetworkEnabled = mLocation.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            int LOCATION_UPDATE_MIN_DISTANCE = 50;
            int LOCATION_UPDATE_MIN_TIME = 1000;
            if (isNetworkEnabled || isGPSEnabled) {
                Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.gettingLocation), Toast.LENGTH_SHORT);
                toast.show();
                if(isGPSEnabled)
                    mLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 50, mLocationListener);
                if(isNetworkEnabled)
                    mLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 100, mLocationListener);
            }
            if (!isNetworkEnabled && !isGPSEnabled) {
                Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.canNotGetLocation), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Latitude = Double.toString(location.getLatitude());
                Longitude = Double.toString(location.getLongitude());
                CDBTHandler.sendEmptyMessage(SEND_GPS_FILTER);
                if (userInfo.getIdentity() == 1) {
                    userInfo.getMember().putLatitude(Latitude);
                    userInfo.getMember().putLongitude(Longitude);
                    UpdateUserLocation updateUserLocation = new UpdateUserLocation();
                    Thread thread = new Thread(updateUserLocation);
                    thread.start();
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    private class StoreRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            storelist.setEnabled(false);
            CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {//根据请求码判断是哪一次申请的权限
            case requestCodeFineLoaction:
                if (grantResults.length > 0) {//grantResults 数组中存放的是授权结果
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//同意授权
                        Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.canNotGetLocation), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        requestUserLocation();
                    }
                }
                break;
            case requestCodeCoarseLocation:
                if (grantResults.length > 0) {//grantResults 数组中存放的是授权结果
                    if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {//同意授权
                        Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.canNotGetLocation), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {

                        requestUserLocation();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class UpdateUserLocation implements Runnable {
        @Override
        public void run() {
            database.UpdateMember(userInfo.getMember());
        }
    }

    private Boolean setupGPS() {
        new AlertDialog.Builder(MainActivity.this).setTitle("GPS服務").setMessage("您尚未開啟定位服務，要前往設定頁面啟動定位服務嗎？")
                .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                returnBool = true;
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(MainActivity.this, "未開啟定位服務，定位將會不準確", Toast.LENGTH_SHORT).show();
                returnBool = false;
            }
        }).show();
        return  returnBool;
    }

    @Override
    protected void onResume() {
        super.onResume();



    }
    //---以上為定位程式---
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchItem);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);//显示提交按钮
        searchView.setOnCloseListener(new SearchView.OnCloseListener(){
            @Override
            public boolean onClose() {
                searchString = "%";
                CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交按钮的点击事件
                searchString = "%"+query+"%";
                CDBTHandler.sendEmptyMessage(SEND_FILTER_REFRESH);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当输入框内容改变的时候回调
                return true;
            }
        });

        return true;
    }
}
