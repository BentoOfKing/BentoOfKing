package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private UserInfo userInfo;
    private long mExitTime;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Database database;
    private Store store[];
    private ListView storelist;
    ArrayList<store_list> storeLists = new ArrayList<store_list>();
    private StoreListViewBaseAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        if(userInfo == null) userInfo = new UserInfo();
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        ConnectDatabase connectDatabase = new ConnectDatabase();
        Thread thread = new Thread(connectDatabase);
        thread.start();

        storelist=(ListView)findViewById(R.id.storeListView);
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new StoreListViewBaseAdapter(storeLists,inflater);
        storelist.setAdapter(adapter);
        StoreListClickHandler storeListClickHandler = new StoreListClickHandler();
        storelist.setOnItemClickListener(storeListClickHandler);
    }
    public class ConnectDatabase implements Runnable{
        @Override
        public void run() {
            database = new Database();
            if(database.GetStoreInit()) store = database.GetStore();
            for(int i=0;i<store.length;i++){
                System.out.println(store[i].getEmail());
                storeLists.add(new store_list(store[i].getStoreName()," * * * * *",100+i+" ","10KM",store[i].getState()));
            }
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

    private class StoreListClickHandler implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this,CheckStoreInfo.class);
            UserInfo storeInfoBundle = new UserInfo();
            storeInfoBundle.setIdentity(2);
            storeInfoBundle.putStore(MainActivity.this.store[position]);
            intent.putExtra("storeInfo",storeInfoBundle);
            startActivity(intent);
        }
    }

}
