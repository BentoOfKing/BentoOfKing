package com.cce.nkfust.tw.bentoofking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

public class RegisterActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private String registerResult;
    private Member memberRegister;
    private Database database;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordCheckEditText;
    private EditText nicknameEditText;
    private RadioButton radio_m;
    private RadioButton radio_f;
    private Button registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListView = (ListView) findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.register));
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        radio_m = findViewById(R.id.radio_m);
        radio_f = findViewById(R.id.radio_f);
        registerButton = findViewById(R.id.registerButton);
        RegisterButtonHandler registerButtonHandler = new RegisterButtonHandler();
        registerButton.setOnClickListener(registerButtonHandler);
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
    public class RegisterButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            String sex;
            //缺判斷是否全部填寫
            if(radio_m.isSelected()){ //radiobutton判斷尚未完成
                sex = "1";
            }else{
                sex = "0";
            }
            memberRegister = new Member(emailEditText.getText().toString(),passwordEditText.getText().toString(),nicknameEditText.getText().toString(),sex,"","","");
            DatabaseMemberRegister databaseMemberRegister = new DatabaseMemberRegister();
            Thread thread = new Thread(databaseMemberRegister);
            thread.start();
            //多執行緒監聽須修正
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //缺根據回傳結果registerResult所對應之動作（回傳結果:"Successful.","An error occurred.","Fail."）


        }
    }
    public class DatabaseMemberRegister implements Runnable{
        @Override
        public void run() {
            database = new Database();
            registerResult = database.MemberRegister(memberRegister);
        }
    }
}
