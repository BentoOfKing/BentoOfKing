package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.SyncFailedException;
import java.util.ArrayList;

public class StoreAppealListActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private Context context;
    private ListView drawerListView,appealListView;
    private DrawerLayout drawerLayout;
    private StoreAppealItemAdapter storeAppealItemAdapter;
    private ArrayList<Appeal> appealArrayList;
    private ProgressDialog progressDialog;
    private Database database;
    private Appeal[] appeal;
    private MainThreadHandler mainThreadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_appeal_list);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.appeal));
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addItem:
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View v = inflater.inflate(R.layout.alertdialog_report, null);
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
                        builder.setTitle("申訴");
                        builder.setView(v);
                        final EditText titleEditText = v.findViewById(R.id.titleEditText);
                        final EditText contentEditText = v.findViewById(R.id.contentEditText);
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("送出",null);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            Appeal appeal;
                            Boolean suc = false;
                            @Override
                            public void onClick(View view) {
                                if(titleEditText.getText().toString().equals("")){
                                    Toast.makeText(context, getResources().getString(R.string.pleaseEnterTitle), Toast.LENGTH_SHORT).show();
                                }else if(contentEditText.getText().toString().equals("")){
                                    Toast.makeText(context, getResources().getString(R.string.pleaseEnterContent), Toast.LENGTH_SHORT).show();
                                }else{
                                    class AddAppeal implements Runnable{
                                        @Override
                                        public void run() {
                                            Database d = new Database();
                                            if(d.AddAppeal(appeal).equals("Successful.")){
                                                suc = true;
                                            }
                                        }
                                    }
                                    appeal = new Appeal();
                                    appeal.putDeclarant(userInfo.getStore().getID());
                                    appeal.putAppealed("");
                                    appeal.putTitle(titleEditText.getText().toString());
                                    appeal.putContent(contentEditText.getText().toString());
                                    appeal.putType("1");
                                    Thread t = new Thread(new AddAppeal());
                                    progressDialog = ProgressDialog.show(context, "請稍等...", "申訴發送中...", true);
                                    t.start();
                                    try {
                                        t.join();
                                        progressDialog.dismiss();
                                        if(suc) {
                                            Toast.makeText(context, "申訴成功", Toast.LENGTH_SHORT).show();
                                            refreshAppeal();
                                        }else{
                                            Toast.makeText(context, "申訴失敗", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (Exception e){
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "申訴失敗", Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        break;
                }
                return false;
            }
        });
        database = new Database();
        mainThreadHandler = new MainThreadHandler();
        appealListView = findViewById(R.id.appealListView);
        appealArrayList = new ArrayList<Appeal>();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        storeAppealItemAdapter = new StoreAppealItemAdapter(inflater,appealArrayList);
        appealListView.setAdapter(storeAppealItemAdapter);
        appealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
                if(!appealArrayList.get((int) l).getResult().equals("")){
                    new AlertDialog.Builder(StoreAppealListActivity.this)
                            .setTitle("處理結果")
                            .setMessage(appealArrayList.get((int) l).getResult())
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });
        refreshAppeal();
    }
    public void refreshAppeal(){
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料載入中...", true);
        appealArrayList.clear();
        database.refreshReviewStoreIndex();
        Thread t = new Thread(new GetAppeal());
        t.start();
    }
    class GetAppeal implements Runnable{
        @Override
        public void run() {
            try {
                appeal = database.GetUserAppeal("1",userInfo.getStore().getID());
                for(int i=0;i<appeal.length;i++){
                    appealArrayList.add(appeal[i]);
                }
                mainThreadHandler.sendEmptyMessage(SUCCESS);
            }catch (Exception e){
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
        }
    }
    public class MainThreadHandler extends Handler {
        public MainThreadHandler(){
            super();
        }
        public MainThreadHandler(Looper looper){
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    storeAppealItemAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                    break;
                case FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);

        }

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_add, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

class StoreAppealItemAdapter extends BaseAdapter {
    private ArrayList<Appeal> appeal;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView stateTextView;
    }

    public StoreAppealItemAdapter(LayoutInflater inflater, ArrayList<Appeal> appeal) {
        this.appeal = appeal;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return appeal.size();
    }

    @Override
    public Appeal getItem(int i) {
        return appeal.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.appeal_item, null);
            viewHolder.titleTextView = view.findViewById(R.id.titleTextView);
            viewHolder.subtitleTextView = view.findViewById(R.id.subtitleTextView);
            viewHolder.stateTextView = view.findViewById(R.id.stateTextView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.titleTextView.setText(getItem(position).getTitle());
        viewHolder.subtitleTextView.setText(getItem(position).getContent());
        if(getItem(position).getResult().equals("")){
            viewHolder.stateTextView.setText("未處理");
        }else{
            viewHolder.stateTextView.setText("已處理");
        }
        return view;
    }
}