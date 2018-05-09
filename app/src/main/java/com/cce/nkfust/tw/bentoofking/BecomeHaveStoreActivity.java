package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

public class BecomeHaveStoreActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int FIND_SUCCESS = 66;
    private static final int FIND_FAIL = 38;
    private static final int UPDATE_SUCCESS = 69;
    private static final int UPDATE_FAIL = 78;
    private UserInfo userInfo;
    private Store store;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Button findButton;
    private Button checkButton;
    private Store retureStore;
    private MainThreadHandler mainThreadHandler;
    private Context context;
    private Boolean isFind;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_become_have_store);
        context = this;
        isFind = false;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        store = (Store) intent.getSerializableExtra(passStoreInfo);
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
        findButton.setOnClickListener(new ButtonHandler());
        checkButton = findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new ButtonHandler());
        mainThreadHandler = new MainThreadHandler();

    }
    private class ButtonHandler implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.findButton:
                    Thread t1 = new Thread(new FindStore());
                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料查詢中...", true);
                    t1.start();
                    break;
                case R.id.checkButton:
                    Thread t2 = new Thread(new CheckButton());
                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                    t2.start();
                    break;
            }
        }
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
            if(addressString.equals("")){
                addressString = "%";
            }else{
                addressString = "%"+addressString+"%";
            }
            if(phoneString.equals("")){
                phoneString = "%";
            }else{
                phoneString = "%"+phoneString+"%";
            }
            Database database = new Database();
            retureStore = null;
            retureStore = database.GetStoreForRegister(nameString,addressString,phoneString);
            if(retureStore != null){
                mainThreadHandler.sendEmptyMessage(FIND_SUCCESS);
            }else{
                mainThreadHandler.sendEmptyMessage(FIND_FAIL);
            }
        }
    }
    private class CheckButton implements Runnable{
        @Override
        public void run() {
            if(isFind){
                retureStore.putEmail(store.getEmail()+"*");
                retureStore.putPassword(store.getPassword());
                retureStore.putState("2");
                retureStore.putNote("由 "+store.getEmail()+" 申請,"+retureStore.getNote());
                Database database = new Database();
                if(database.UpdateStore(retureStore).equals("Successful.")){
                    mainThreadHandler.sendEmptyMessage(UPDATE_SUCCESS);
                }else{
                    mainThreadHandler.sendEmptyMessage(UPDATE_FAIL);
                }

            }
        }
    }

    private class MainThreadHandler extends Handler {
        public MainThreadHandler(){
            super();
        }
        public MainThreadHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case FIND_SUCCESS:
                    nameEditText.setText(retureStore.getStoreName());
                    addressEditText.setText(retureStore.getAddress());
                    phoneEditText.setText(retureStore.getPhone());
                    isFind = true;
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.findSuc), Toast.LENGTH_SHORT).show();
                    break;
                case FIND_FAIL:
                    isFind = false;
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.findFail), Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.storeRegisterSuc), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(context,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(passUserInfo,userInfo);
                    context.startActivity(intent);
                    finish();
                    break;
                case UPDATE_FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.storeRegisterFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(findViewById(R.id.drawerListView)))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }
}
