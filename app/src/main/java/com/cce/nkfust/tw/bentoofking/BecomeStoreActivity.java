package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

public class BecomeStoreActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Button haveStoreButton,notHaveStoreButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_become_store);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout);
        toolbar.setTitle(getResources().getString(R.string.becomeStore));
        haveStoreButton = findViewById(R.id.loginButton);
        notHaveStoreButton = findViewById(R.id.notHaveStoreButton);
        ButtonHandler buttonHandler = new ButtonHandler();
        haveStoreButton.setOnClickListener(buttonHandler);
        notHaveStoreButton.setOnClickListener(buttonHandler);
    }
    public class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            switch (view.getId()){
                case R.id.loginButton:
                    intent.setClass(BecomeStoreActivity.this,BecomeHaveStoreActivity.class);
                    startActivity(intent);
                    break;
                case R.id.notHaveStoreButton:
                    intent.setClass(BecomeStoreActivity.this,BecomeNotHaveStoreActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;

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
