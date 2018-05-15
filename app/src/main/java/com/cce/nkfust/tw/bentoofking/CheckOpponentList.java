package com.cce.nkfust.tw.bentoofking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class CheckOpponentList extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int REFRESH_ACTIVITY = 5278, requestCodeFineLoaction = 1, requestCodeCoarseLocation = 2, MORE_STORE = 5279, REFRESH_STORELIST = 5273, SEND_FILTER_REFRESH = 5274, SEND_LAST_FILTER = 5275, SEND_GPS_FILTER = 5276, REFRESHING = 5277;
    private static final int GET_USERINFO = 6667,CREATE_DRAWER = 6668,INFO_RESULT = 6669;
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
    private SwipeRefreshLayout swipeLayout;
    private int distanceState = 0, rankState = 1, priceState = 0, distanceKm = 10;
    private Context context;
    private String searchString = "%";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_opponent_list);
        InfoReceive();
        varialbleSetup();
        UIconnect();
        UIsetup();
        UIhandle();
        UIupdate();
    }

    private void varialbleSetup() {
        database = new Database();
        mainHandler = new MainThreadHandler(Looper.getMainLooper());
        CDBThread = new HandlerThread("1stThread");
        CDBThread.start();
        CDBTHandler = new Handler_A(CDBThread.getLooper());
        storeLists = new ArrayList<store_list>();
        adapter = new StoreListViewBaseAdapter(CheckOpponentList.this, storeLists, (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    private void InfoReceive() {
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if (userInfo == null) userInfo = new UserInfo();
    }

    private void UIconnect() {
        context = this;
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        storelist = findViewById(R.id.opponent);
        swipeLayout = findViewById(R.id.swipeLayout);
    }

    private void UIhandle() {
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
        storelist.setAdapter(adapter);
    }

    private void UIupdate() {
        CDBTHandler.sendEmptyMessage(REFRESH_STORELIST);
        mainHandler.sendEmptyMessage(CREATE_DRAWER);
    }



    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
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
                    if (!swipeLayout.isRefreshing()) mainHandler.sendEmptyMessage(REFRESHING);
                    store = database.GetStoreByPosition(CheckOpponentList.this.searchString,userInfo.getStore().getLongitude(), userInfo.getStore().getLatitude(), CheckOpponentList.this.distanceState, CheckOpponentList.this.rankState, CheckOpponentList.this.priceState, CheckOpponentList.this.distanceKm);
                    for (int i = 0; i < store.length; i++) {
                        store_list additem = new store_list(store[i]);
                        if ((!msg.getData().getBoolean("businessStateNow")) || additem.getStatus().equals("營業中"))
                            storeLists.add(new store_list(store[i]));
                    }
                    mainHandler.sendEmptyMessage(REFRESH_ACTIVITY);
                    break;
                case REFRESH_STORELIST:
                    if (!swipeLayout.isRefreshing()) mainHandler.sendEmptyMessage(REFRESHING);
                    database.refreshStoreIndex();
                    store = database.GetStoreByPosition(CheckOpponentList.this.searchString,userInfo.getStore().getLongitude(), userInfo.getStore().getLatitude(), CheckOpponentList.this.distanceState, CheckOpponentList.this.rankState, CheckOpponentList.this.priceState, CheckOpponentList.this.distanceKm);
                    storeLists.clear();
                    for (int i = 0; i < store.length; i++) {
                        store_list additem = new store_list(store[i]);
                        if ((!msg.getData().getBoolean("businessStateNow")) || additem.getStatus().equals("營業中"))
                            storeLists.add(new store_list(store[i]));
                    }
                    mainHandler.sendEmptyMessage(REFRESH_ACTIVITY);
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
                    drawer.init(CheckOpponentList.this, toolbar, drawerListView, drawerLayout, userInfo);
                    toolbar.setTitle("對手清單");
                    break;
            }
            super.handleMessage(msg);
        }
    }


    private class StoreListClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(CheckOpponentList.this, CheckStoreInfo.class);
            UserInfo storeInfoBundle = new UserInfo();
            storeInfoBundle.setIdentity(2);
            storeInfoBundle.putStore(CheckOpponentList.this.storeLists.get(position).getStoreInfo());
            intent.putExtra("storeInfo", storeInfoBundle);
            intent.putExtra(CheckOpponentList.passUserInfo, CheckOpponentList.this.userInfo);
            startActivityForResult(intent,INFO_RESULT);
        }
    }


    private class StoreListScrollHandler implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    CDBTHandler.sendEmptyMessage(MORE_STORE);
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    //---以下為定位程式---

    private class StoreRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            storelist.setEnabled(false);
            CDBTHandler.sendEmptyMessage(REFRESH_STORELIST);
        }
    }

}
