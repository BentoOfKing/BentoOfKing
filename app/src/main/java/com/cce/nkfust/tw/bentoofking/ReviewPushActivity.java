package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ReviewPushActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int GET_FAIL = 1;
    private static final int ADD_FAIL = 2;
    private static final int GET_SUCCESS = 3;
    private static final int EDIT_SUCCESS = 4;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,pushListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private Database database;
    private Push editPush;
    private MainThreadHandler mainThreadHandler;
    private ArrayList<PushInfo> pushInfos;
    private ProgressDialog progressDialog;
    private ArrayList<Push> pushList;
    private ArrayList<Store> pushStoreList;
    private ReviewPushAdapter reviewPushAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_push);
        mainThreadHandler = new MainThreadHandler();
        context = this;
        database = new Database();
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.pushmanage));
        pushListView = findViewById(R.id.pushListView);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pushInfos = new ArrayList<PushInfo>();
        pushList = new ArrayList<Push>();
        pushStoreList = new ArrayList<Store>();
        reviewPushAdapter = new ReviewPushAdapter(inflater,pushInfos);
        pushListView.setAdapter(reviewPushAdapter);
        pushListView.setOnItemClickListener(new ItemClickHandler());
        reflashList();
    }

    class ItemClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
            String[] item = getResources().getStringArray(R.array.adminPushArray);
            new AlertDialog.Builder(context).setItems(item, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0://編輯推播
                            LayoutInflater inflater = LayoutInflater.from(context);
                            View v = inflater.inflate(R.layout.alertdialog_push, null);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(getResources().getString(R.string.editPush));
                            builder.setView(v);
                            final EditText editText = v.findViewById(R.id.contentEditText);
                            editText.setText(pushList.get((int)l).getContent());
                            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {dialog.dismiss();}
                            });
                            builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    editPush = new Push(pushList.get((int)l).getID(),pushList.get((int)l).getStore(),editText.getText().toString(),"1","");
                                    Thread t = new Thread(new UpdatePush());
                                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                    t.start();
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            break;
                        case 1:
                            Intent intent = new Intent();
                            intent.setClass(context,CheckStoreInfo.class);
                            intent.putExtra(passUserInfo,userInfo);
                            UserInfo tmp = userInfo;
                            tmp.putStore(pushStoreList.get((int)l));
                            intent.putExtra("storeInfo",tmp);
                            context.startActivity(intent);
                            break;
                        case 2://審核通過
                            editPush = new Push(pushList.get((int)l).getID(),pushList.get((int)l).getStore(),pushList.get((int)l).getContent().toString(),"2","");
                            Thread t = new Thread(new UpdatePush());
                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                            t.start();
                            dialog.dismiss();
                            break;
                    }
                }
            }).show();
        }
    }
    class GetPush implements Runnable{
        @Override
        public void run() {
            try {
                Push[] push = database.GetPush("%", "1");
                if (push.length != 0) {
                    for (int i = 0; i < push.length; i++) {
                        pushList.add(push[i]);
                        Store LocalStore = database.GetSingleStore(push[i].getStore());
                        pushStoreList.add(LocalStore);
                        pushInfos.add(new PushInfo(push[i],LocalStore));
                    }
                }
                mainThreadHandler.sendEmptyMessage(GET_SUCCESS);
            }catch(Exception e){
                mainThreadHandler.sendEmptyMessage(GET_FAIL);
            }
        }
    }
    class UpdatePush implements Runnable{
        @Override
        public void run() {
            if(database.UpdatePush(editPush).equals("Successful.")){
                mainThreadHandler.sendEmptyMessage(EDIT_SUCCESS);
            }else{
                mainThreadHandler.sendEmptyMessage(ADD_FAIL);
            }
        }
    }

    public class MainThreadHandler extends Handler {
        public MainThreadHandler() {
            super();
        }
        public MainThreadHandler(Looper looper) {
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EDIT_SUCCESS:
                    progressDialog.dismiss();
                    reflashList();
                    reviewPushAdapter.notifyDataSetChanged();
                    break;
                case ADD_FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.addFail), Toast.LENGTH_SHORT).show();
                    break;
                case GET_SUCCESS:
                    progressDialog.dismiss();
                    reviewPushAdapter.notifyDataSetChanged();
                    break;
                case GET_FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.connectError), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }

    void reflashList(){
        pushInfos.clear();
        pushList.clear();
        pushStoreList.clear();
        database.refreshStoreIndex();
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
        Thread t = new Thread(new GetPush());
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class PushInfo{
        private Push push;
        private Store store;
        public PushInfo( Push push,Store store ){
            this.push = push;
            this.store = store;
        }
        public Push getPush(){ return push; }
        public Store getStore(){ return store; }
    }

}
class ReviewPushAdapter extends BaseAdapter {
    private ArrayList<ReviewPushActivity.PushInfo> pushInfos;
    private ArrayList<Push> push;
    private ArrayList<Store> store;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        RelativeLayout relativeLayout;
        TextView contentTextView;
        TextView stateTextView;
    }

    public ReviewPushAdapter(LayoutInflater inflater, ArrayList<ReviewPushActivity.PushInfo> infoList) {
        this.pushInfos = infoList;
        this.inflater = inflater;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() { return pushInfos.size(); }

    @Override
    public ReviewPushActivity.PushInfo getItem(int i) { return pushInfos.get(i); }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.push_item, null);
            viewHolder.contentTextView = view.findViewById(R.id.contentTextView);
            viewHolder.stateTextView = view.findViewById(R.id.stateTextView);
            viewHolder.relativeLayout = view.findViewById(R.id.relativeLayout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.contentTextView.setText(getItem(position).getPush().getContent());
        viewHolder.stateTextView.setText(getItem(position).getStore().getStoreName());
        return view;
    }
}