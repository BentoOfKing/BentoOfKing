package com.cce.nkfust.tw.bentoofking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AppealCommentActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passAppealInfo = "APPEAL_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private static final int MORE_STORE = 39;
    private static final int REFRESH_ACTIVITY = 40;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ListView appealListView;
    private ArrayList<Appeal> appealArrayList;
    private Context context;
    private MainThreadHandler mainThreadHandler;
    private AppealCommentAdapter appealCommentAdapter;
    private Handler_A anotherHandler;
    private HandlerThread anotherThread;
    private Appeal[] appeal;
    private Database database;
    private Appeal beUpdateAppeal;
    private ProgressDialog progressDialog;
    private Boolean firstOnResume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal_comment);
        firstOnResume = true;
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        appealArrayList = new ArrayList<Appeal>();
        Drawer drawer = new Drawer();
        drawerListView = findViewById(R.id.drawerListView);
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.appelaComment));
        appealListView = findViewById(R.id.appealListView);
        appealListView.setOnScrollListener(new AppealListScrollHandler());
        mainThreadHandler = new MainThreadHandler();
        anotherThread = new HandlerThread("1stThread");
        anotherThread.start();
        database = new Database();
        anotherHandler = new Handler_A(anotherThread.getLooper());
        Thread thread = new Thread(new GetAppeal());
        thread.start();
    }
    class GetAppeal implements Runnable{
        @Override
        public void run() {
            try {
                database.refreshStoreIndex();
                appeal = database.GetAppeal("3");
                appealArrayList.clear();
                for(int i=0;i<appeal.length;i++){
                    appealArrayList.add(appeal[i]); //1.ArrayList
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
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    appealCommentAdapter = new AppealCommentAdapter(inflater,appealArrayList);
                    final String[] item = getResources().getStringArray(R.array.previewStoreArray);
                    appealListView.setAdapter(appealCommentAdapter);//3.設Adapter
                    appealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
                            Intent intent = new Intent();
                            intent.setClass(context,CheckStoreErrorActivity.class);
                            intent.putExtra(passUserInfo,userInfo);
                            intent.putExtra(passAppealInfo,appealArrayList.get((int) l));
                            startActivity(intent);
                        }
                    });
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
                case REFRESH_ACTIVITY:
                    appealCommentAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);

        }

    }

    private class Handler_A extends Handler {
        public Handler_A(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MORE_STORE:
                    appeal = database.GetAppeal("3");
                    for(int i=0;i<appeal.length;i++){
                        appealArrayList.add(appeal[i]); //1.ArrayList
                    }
                    mainThreadHandler.sendEmptyMessage(REFRESH_ACTIVITY);
                    break;
            }
        }
    }

    private class AppealListScrollHandler implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    anotherHandler.sendEmptyMessage(MORE_STORE);
                }
            }
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!firstOnResume){
            appealArrayList.clear();
            database = null;
            database = new Database();
            anotherHandler.sendEmptyMessage(MORE_STORE);
        }
        firstOnResume = false;
    }
}

class AppealCommentAdapter extends BaseAdapter { //2.建Adapter
    private ArrayList<Appeal> appeal;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        LinearLayout appealLinearLayout;
        TextView titleTextView;
        TextView subtitleTextView;
    }

    public AppealCommentAdapter(LayoutInflater inflater, ArrayList<Appeal> appeal) {
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
            view = this.inflater.inflate(R.layout.admin_item, null);
            viewHolder.titleTextView = view.findViewById(R.id.titleTextView);
            viewHolder.subtitleTextView = view.findViewById(R.id.subtitleTextView);
            viewHolder.appealLinearLayout = view.findViewById(R.id.LinearLayout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.titleTextView.setText(getItem(position).getTitle());
        viewHolder.subtitleTextView.setText(getItem(position).getContent());
        return view;
    }
}