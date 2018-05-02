package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class BecomeStoreActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private Store storeRegister;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Button haveStoreButton,notHaveStoreButton;
    private EditText emailEditText,passwordEditText,passwordCheckEditText;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_become_store);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.becomeStore));
        haveStoreButton = findViewById(R.id.storeLoginButton);
        notHaveStoreButton = findViewById(R.id.notHaveStoreButton);
        ButtonHandler buttonHandler = new ButtonHandler();
        haveStoreButton.setOnClickListener(buttonHandler);
        notHaveStoreButton.setOnClickListener(buttonHandler);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText);
    }
    public class ButtonHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if(emailEditText.getText().toString().equals("")){
                Toast.makeText(context,getResources().getString(R.string.emailError), Toast.LENGTH_SHORT).show();
                return;
            }else if (passwordEditText.getText().toString().equals("") || !passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())){
                Toast.makeText(context,getResources().getString(R.string.passwordError), Toast.LENGTH_SHORT).show();
                return;
            }
            Store store = new Store();
            store.putEmail(emailEditText.getText().toString());
            store.putPassword(passwordEditText.getText().toString());
            Intent intent = new Intent();
            switch (view.getId()){
                case R.id.storeLoginButton:
                    intent.setClass(BecomeStoreActivity.this,BecomeHaveStoreActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    intent.putExtra(passStoreInfo,store);
                    startActivity(intent);
                    break;
                case R.id.notHaveStoreButton:
                    intent.setClass(BecomeStoreActivity.this,BecomeNotHaveStoreActivity.class);
                    intent.putExtra(passUserInfo,userInfo);
                    intent.putExtra(passStoreInfo,store);
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
