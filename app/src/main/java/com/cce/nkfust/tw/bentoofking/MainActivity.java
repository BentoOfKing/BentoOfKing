package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        creatHandler();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if(userInfo == null) userInfo = new UserInfo();
        toolbar = findViewById(R.id.toolbar);


        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        storelist=(ListView)findViewById(R.id.storeListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        ConnectDatabase connectDatabase = new ConnectDatabase();
        CDBTHandler.post(connectDatabase);

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
    public class ConnectDatabase implements Runnable{
        @Override
        public void run() {
            database = new Database();
            if(database.GetStoreInit()) store = database.GetStore();
            for(int i=0;i<store.length;i++){
                System.out.println(store[i].getEmail());
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
