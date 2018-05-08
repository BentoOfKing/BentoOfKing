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
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewStoreActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passStoreInfo = "STORE_INFO";
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private static final int MORE_STORE = 39;
    private static final int REFRESH_ACTIVITY = 40;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ListView storeListView;
    private ArrayList<Store> storeArrayList;
    private Context context;
    private MainThreadHandler mainThreadHandler;
    private ReviewStoreAdapter reviewStoreAdapter;
    private Handler_A anotherHandler;
    private HandlerThread anotherThread;
    private Store[] store;
    private Database database;
    private Store beUpdateStore;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_store);
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        storeArrayList = new ArrayList<Store>();
        Drawer drawer = new Drawer();
        drawerListView = findViewById(R.id.drawerListView);
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.reviewStore));
        storeListView = findViewById(R.id.storeListView);
        storeListView.setOnScrollListener(new StoreListScrollHandler());
        mainThreadHandler = new MainThreadHandler();
        anotherThread = new HandlerThread("1stThread");
        anotherThread.start();
        database = new Database();
        anotherHandler = new Handler_A(anotherThread.getLooper());
        Thread thread = new Thread(new GetStore());
        thread.start();
    }
    class GetStore implements Runnable{
        @Override
        public void run() {
            try {
                database.refreshStoreIndex();
                store = database.GetReviewStore();
                storeArrayList.clear();
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
                    final String[] item = getResources().getStringArray(R.array.previewStoreArray);
                    storeListView.setAdapter(reviewStoreAdapter);//3.設Adapter
                    storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        LayoutInflater inflater;
                        AlertDialog alertDialog;
                        EditText editText;
                        Intent intent;
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
                            new AlertDialog.Builder(context)
                                    .setItems(item, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case 0://審核資訊
                                                    intent = new Intent();
                                                    intent.setClass(context,PreviewStoreInfoActivity.class);
                                                    intent.putExtra(passUserInfo,userInfo);
                                                    intent.putExtra(passStoreInfo,storeArrayList.get((int)l));
                                                    startActivity(intent);
                                                    break;
                                                case 1://審核菜單
                                                    intent = new Intent();
                                                    intent.setClass(context,EditExistedMenuActivity.class);
                                                    intent.putExtra(passUserInfo,userInfo);
                                                    intent.putExtra(passStoreInfo,storeArrayList.get((int)l));
                                                    startActivity(intent);
                                                    break;
                                                case 2://審核照片
                                                    intent = new Intent();
                                                    intent.setClass(context,EditExistedPhotoActivity.class);
                                                    intent.putExtra(passUserInfo,userInfo);
                                                    intent.putExtra(passStoreInfo,storeArrayList.get((int)l));
                                                    startActivity(intent);
                                                    break;
                                                case 3://備註
                                                    inflater = LayoutInflater.from(context);
                                                    View view = inflater.inflate(R.layout.alertdialog_note, null);
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setTitle(getResources().getString(R.string.note));
                                                    builder.setView(view);
                                                    editText = view.findViewById(R.id.noteEditText);
                                                    editText.setText(storeArrayList.get((int)l).getNote());
                                                    builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            storeArrayList.get((int)l).putNote(editText.getText().toString());
                                                            beUpdateStore=storeArrayList.get((int)l);
                                                            Thread t = new Thread(new UpdateStore());
                                                            t.start();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    alertDialog = builder.create();
                                                    alertDialog.show();
                                                    break;
                                                case 4://不通過
                                                    break;
                                                case 5://通過
                                                    storeArrayList.get((int)l).putState("1");
                                                    beUpdateStore=storeArrayList.get((int)l);
                                                    Thread t = new Thread(new UpdateStore());
                                                    t.start();
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        }
                    });
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
                case REFRESH_ACTIVITY:
                    reviewStoreAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);

        }

    }

    private class UpdateStore implements Runnable {

        @Override
        public void run() {
            if(database.UpdateStore(beUpdateStore).equals("Successful.")){
                database = null;
                database = new Database();
                Thread thread = new Thread(new GetStore());
                thread.start();
            }else{
                mainThreadHandler.sendEmptyMessage(FAIL);
            }
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
                    store = database.GetReviewStore();
                    for(int i=0;i<store.length;i++){
                        storeArrayList.add(store[i]); //1.ArrayList
                    }
                    mainThreadHandler.sendEmptyMessage(REFRESH_ACTIVITY);
                    break;
            }
        }
    }

    private class StoreListScrollHandler implements AbsListView.OnScrollListener {
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