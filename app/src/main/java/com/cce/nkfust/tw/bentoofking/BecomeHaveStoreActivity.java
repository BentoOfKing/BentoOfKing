package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BecomeHaveStoreActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Button findButton;
    private Button checkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_become_have_store);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        drawer.setToolbarNavigation();
        toolbar.setTitle(getResources().getString(R.string.becomeStore));
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        findButton = findViewById(R.id.findButton);
        checkButton = findViewById(R.id.checkButton);

    }
    private class FindStore implements Runnable{
        @Override
        public void run() {
            String nameString = nameEditText.getText().toString();
            String addressString = addressEditText.getText().toString();
            String phoneString = phoneEditText.getText().toString();
            if(nameString.equals("")){
                nameString = "%";
            }else{
                nameString = "%"+nameString+"%";
            }

        }
    }
    private class CheckButton implements Runnable{
        @Override
        public void run() {

        }
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}
