package com.cce.nkfust.tw.bentoofking;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewStoreActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ListView storeListView;
    private ArrayList<Store> storeArrayList;
    private Context context;
    private MainThreadHandler mainThreadHandler;
    private ReviewStoreAdapter reviewStoreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_store);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerListView = findViewById(R.id.drawerListView);
        Drawer drawer = new Drawer();
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.reviewStore));
        storeListView = findViewById(R.id.storeListView);
        mainThreadHandler = new MainThreadHandler();
        Thread thread = new Thread(new GetStore());
        thread.start();
    }
    class GetStore implements Runnable{
        @Override
        public void run() {
            try {
                Database database = new Database();
                Store[] store = database.GetReviewStore();
                storeArrayList = new ArrayList<Store>();
                for(int i=0;i<store.length;i++){
                    storeArrayList.add(store[i]); //1.ArrayList
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
                    reviewStoreAdapter = new ReviewStoreAdapter(inflater,storeArrayList);
                    storeListView.setAdapter(reviewStoreAdapter);//3.設Adapter
                    storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //連到下一頁
                            Toast.makeText(context, storeArrayList.get((int)l).getStoreName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);

        }

    }
}

class ReviewStoreAdapter extends BaseAdapter { //2.建Adapter
    private ArrayList<Store> store;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        LinearLayout storeLinearLayout;
        TextView storeTextView;
        TextView noteTextView;
    }

    public ReviewStoreAdapter(LayoutInflater inflater, ArrayList<Store> store) {
        this.store = store;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return store.size();
    }

    @Override
    public Store getItem(int i) {
        return store.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(R.layout.review_store_item, null);
            viewHolder.storeTextView = view.findViewById(R.id.storeTextView);
            viewHolder.noteTextView = view.findViewById(R.id.noteTextView);
            viewHolder.storeLinearLayout = view.findViewById(R.id.storeLinearLayout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.storeTextView.setText(getItem(position).getStoreName());
        viewHolder.noteTextView.setText(getItem(position).getNote());
        return view;
    }
}