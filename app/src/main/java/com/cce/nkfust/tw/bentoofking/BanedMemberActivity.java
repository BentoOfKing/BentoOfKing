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

public class BanedMemberActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static String passMemberInfo = "MEMBER_INFO";
    private static final int SUCCESS = 66;
    private static final int FAIL = 38;
    private static final int MORE_STORE = 39;
    private static final int REFRESH_ACTIVITY = 40;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ListView appealListView;
    private ArrayList<Member> memberArrayList;
    private Context context;
    private MainThreadHandler mainThreadHandler;
    private MemberAdapter memberAdapter;
    private Handler_A anotherHandler;
    private HandlerThread anotherThread;
    private Member[] member;
    private Database database;
    private Appeal beUpdateAppeal;
    private ProgressDialog progressDialog;
    private Boolean firstOnResume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baned_member);
        firstOnResume = true;
        context = this;
        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra(passUserInfo);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        memberArrayList = new ArrayList<Member>();
        Drawer drawer = new Drawer();
        drawerListView = findViewById(R.id.drawerListView);
        drawer.init(this,toolbar,drawerListView,drawerLayout,userInfo);
        toolbar.setTitle(getResources().getString(R.string.banMember));
        appealListView = findViewById(R.id.appealListView);
        appealListView.setOnScrollListener(new AppealListScrollHandler());
        mainThreadHandler = new MainThreadHandler();
        anotherThread = new HandlerThread("1stThread");
        anotherThread.start();
        database = new Database();
        anotherHandler = new Handler_A(anotherThread.getLooper());
        Thread thread = new Thread(new GetMember());
        thread.start();
    }
    class GetMember implements Runnable{
        @Override
        public void run() {
            try {
                database.refreshStoreIndex();
                member = database.GetBanedMember();
                memberArrayList.clear();
                for(int i=0;i<member.length;i++){
                    memberArrayList.add(member[i]); //1.ArrayList
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
                    memberAdapter = new MemberAdapter(inflater,memberArrayList);
                    final String[] item = getResources().getStringArray(R.array.previewStoreArray);
                    appealListView.setAdapter(memberAdapter);//3.設Adapter
                    appealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
                            Intent intent = new Intent();
                            intent.setClass(context,CheckMemberActivity.class);
                            intent.putExtra(passUserInfo,userInfo);
                            intent.putExtra(passMemberInfo,memberArrayList.get((int) l));
                            startActivity(intent);
                        }
                    });
                    break;
                case FAIL:
                    Toast.makeText(context, getResources().getString(R.string.loadFail), Toast.LENGTH_SHORT).show();
                    break;
                case REFRESH_ACTIVITY:
                    memberAdapter.notifyDataSetChanged();
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
                    member = database.GetBanedMember();
                    for(int i=0;i<member.length;i++){
                        memberArrayList.add(member[i]); //1.ArrayList
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
            memberArrayList.clear();
            database = null;
            database = new Database();
            anotherHandler.sendEmptyMessage(MORE_STORE);
        }
        firstOnResume = false;
    }
}

class MemberAdapter extends BaseAdapter { //2.建Adapter
    private ArrayList<Member> member;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        LinearLayout memberLinearLayout;
        TextView titleTextView;
        TextView subtitleTextView;
    }

    public MemberAdapter(LayoutInflater inflater, ArrayList<Member> member) {
        this.member = member;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return member.size();
    }

    @Override
    public Member getItem(int i) {
        return member.get(i);
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
            viewHolder.memberLinearLayout = view.findViewById(R.id.LinearLayout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.titleTextView.setText(getItem(position).getEmail());
        viewHolder.subtitleTextView.setText(getItem(position).getNote());
        return view;
    }
}