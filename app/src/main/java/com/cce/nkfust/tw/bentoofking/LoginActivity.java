package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView loginPrompt;
    private Button loginButton;
    private Database database;
    private Member member;

    private Handler handler = new Handler();
    private ProgressDialog progressDialog;
    private Handler LoginThreadHandler;
    private HandlerThread LoginThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.memberLogin));
        emailEditText =  findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginPrompt = findViewById(R.id.loginPrompt);
        loginButton = findViewById(R.id.storeLoginButton);
        LoginButtonHandler loginButtonHandler = new LoginButtonHandler();
        loginButton.setOnClickListener(loginButtonHandler);



    }
    public class LoginButtonHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            DatabaseLogin databaseLogin = new DatabaseLogin();
            LoginThread = new HandlerThread("Login1");
            progressDialog = ProgressDialog.show(LoginActivity.this, "請稍等...", "會員登入中...", true);
            LoginThread.start();
            LoginThreadHandler = new Handler(LoginThread.getLooper());
            LoginThreadHandler.post(databaseLogin);


        }
    }



    public class DatabaseLogin implements Runnable{
        @Override
        public void run() {
            Handler LoginUiHandler = new Handler();  //修改
            LoginConfirm  loginconfirm  = new LoginConfirm();
            database = new Database();
            member = database.MemberLogin(emailEditText.getText().toString(),passwordEditText.getText().toString());
            LoginUiHandler.post(loginconfirm);
        }
    }

    public class LoginConfirm implements Runnable{
        @Override
        public void run() {
            if(member != null) {
                if (member.getEmail().equals("Password error.")) {
                    Toast toast = Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.passwordError), Toast.LENGTH_LONG);
                    toast.show();
                    Handler handler = new Handler();
                    progressDialog.dismiss();
                }else if(member.getEmail().equals("Email error.")){
                    Toast toast = Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.emailError), Toast.LENGTH_LONG);
                    toast.show();
                    progressDialog.dismiss();
                }else {
                    Toast toast = Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.loginSuccessful), Toast.LENGTH_LONG);
                    toast.show();
                    userInfo = new UserInfo();
                    userInfo.putMember(member);
                    userInfo.setIdentity(1);
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);//有修改
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo, userInfo);

                    /////
                    String NumberTemp = member.getEmail();
                    String PasswordTemp = member.getPassword();


                    SharedPreferences LoginRecord = getApplication().
                            getSharedPreferences("LoginRecord", Context.MODE_PRIVATE);

                    LoginRecord.edit()
                            .putString("Recordemail",NumberTemp)
                            .putString("Recordpassword",PasswordTemp)
                            .putInt("RecordFlag",1)
                            .commit();

                    /////

                    progressDialog.dismiss();
                    startActivity(intent);

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
}

