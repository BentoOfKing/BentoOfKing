package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class StoreLoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView loginPrompt;
    private Button storeLoginButton;
    private Database database;
    private Store store;
    private Admin admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_login);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout);
        toolbar.setTitle(getResources().getString(R.string.storeLogin));
        emailEditText =  findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginPrompt = findViewById(R.id.loginPrompt);
        storeLoginButton = findViewById(R.id.storeLoginButton);
        LoginButtonHandler loginButtonHandler = new LoginButtonHandler();
        storeLoginButton.setOnClickListener(loginButtonHandler);

    }
    public class LoginButtonHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            DatabaseLogin databaseLogin = new DatabaseLogin();
            Thread thread = new Thread(databaseLogin);

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(store!=null){
                Intent intent = new Intent();
                intent.setClass(StoreLoginActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else if(admin!=null){
                Intent intent = new Intent();
                intent.setClass(StoreLoginActivity.this , MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);;
            }else{
                loginPrompt.setText("登入資料錯誤");

            }








        }
    }

    public class DatabaseLogin implements Runnable{
        @Override
        public void run() {
            database = new Database();
            store = database.StoreLogin(emailEditText.getText().toString(),passwordEditText.getText().toString());
            if(store == null) admin = database.AdminLogin(emailEditText.getText().toString(),passwordEditText.getText().toString());
        }
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}
