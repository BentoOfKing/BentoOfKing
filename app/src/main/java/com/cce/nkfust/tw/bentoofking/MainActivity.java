package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] visitorMenuItem = {getResources().getString(R.string.login),getResources().getString(R.string.register),getResources().getString(R.string.becomeStore),getResources().getString(R.string.about)};
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("便當王");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this,R.color.white));
        drawerLayout.addDrawerListener(drawerToggle);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        drawerListView.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item,visitorMenuItem));
        drawerListView.setOnItemClickListener(drawerListener);
    }
    private ListView.OnItemClickListener drawerListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent();
            switch (i){
                case 0:
                    //登入
                    break;
                case 1:
                    //註冊
                    intent.setClass(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    //MainActivity.this.finish();
                    break;
                case 2:
                    //成為店家
                    break;
                case 3:
                    //關於
                    break;
                default:
                    break;
            }
        }
    };
}
