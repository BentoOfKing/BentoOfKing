package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StoreLoginActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView loginPrompt;
    private Button storeLoginButton;
    private Database database;
    private Database initial;
    private Store store;
    private Admin admin;
    private Handler StoreLoginThreadHandler;
    private HandlerThread StoreLoginLoginThread;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_login);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
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

         //多執行緒

            StoreLoginLoginThread = new HandlerThread("StoreLogin1");
            StoreLoginLoginThread.start();
            StoreLoginThreadHandler = new Handler(StoreLoginLoginThread.getLooper());
            StoreLoginThreadHandler.post(databaseLogin);
            progressDialog = ProgressDialog.show(StoreLoginActivity.this, "請稍等...", "店家登入中...", true);

        }
    }

    public class DatabaseLogin implements Runnable{
        @Override
        public void run() {

            Handler StoreLoginUiHandler = new Handler();  //修改
            StoreLoginConfirm storeLoginConfirm = new StoreLoginConfirm();

            database = new Database();
            initial = new  Database();
            store = database.StoreLogin(emailEditText.getText().toString(),passwordEditText.getText().toString());
            if( store.getEmail().equals("Email error.") && (admin == null|| admin.getEmail().equals("Email error.")|| admin.getEmail().equals("Password error."))){
                admin = database.AdminLogin(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
            StoreLoginUiHandler.post(storeLoginConfirm);
        }
    }

    public class StoreLoginConfirm implements Runnable{

        @Override
        public void run() {

            if (store.getEmail().equals("Password error.")&&(admin==null||admin.getEmail().equals("Email error."))) {
                Toast toast = Toast.makeText(StoreLoginActivity.this,
                        getResources().getString(R.string.passwordError), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
            }else if(store.getEmail().equals("Email error.")&&(admin==null||admin.getEmail().equals("Email error."))){
                Toast toast = Toast.makeText(StoreLoginActivity.this,
                        getResources().getString(R.string.emailError), Toast.LENGTH_LONG);  //全空白
                toast.show();
                progressDialog.dismiss();
            }else if(store.getEmail().equals("Email error.")&&admin.getEmail().equals("Email error.")){
                Toast toast = Toast.makeText(StoreLoginActivity.this,
                        getResources().getString(R.string.emailError), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
            }else if(store.getEmail().equals("Email error.")&&admin.getEmail().equals("Password error.")){
                Toast toast = Toast.makeText(StoreLoginActivity.this,
                        getResources().getString(R.string.passwordError), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
            }


            else {
                Toast toast = Toast.makeText(StoreLoginActivity.this,
                        getResources().getString(R.string.loginSuccessful), Toast.LENGTH_LONG);
                toast.show();
                progressDialog.dismiss();
                if(admin==null||admin.getEmail().equals("Email error.")){
                    userInfo = new UserInfo();
                    userInfo.setIdentity(2);
                    userInfo.putStore(store);
                    Intent intent = new Intent();
                    intent.setClass(StoreLoginActivity.this , MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    startActivity(intent);
                }else if(admin!=null){
                    userInfo = new UserInfo();
                    userInfo.setIdentity(3);
                    userInfo.putAdmin(admin);
                    Intent intent = new Intent();
                    intent.setClass(StoreLoginActivity.this , MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    startActivity(intent);;
                }else{
                    loginPrompt.setText("登入資料錯誤");

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


    //身分別




}
