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

import java.util.ArrayList;

public class PushManageActivity extends AppCompatActivity {
    private static String passUserInfo = "USER_INFO";
    private static final int ADD_SUCCESS = 1;
    private static final int ADD_FAIL = 2;
    private static final int GET_SUCCESS = 3;
    private static final int EDIT_SUCCESS = 4;
    private UserInfo userInfo;
    private Toolbar toolbar;
    private ListView drawerListView,pushListView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton floatingActionButton;
    private Context context;
    private Database database;
    private Push newPush,editPush;
    private MainThreadHandler mainThreadHandler;
    private ProgressDialog progressDialog;
    private ArrayList<Push> pushList;
    private PushAdapter pushAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_manage);
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
        toolbar.setTitle(getResources().getString(R.string.pushManage));
        pushListView = findViewById(R.id.pushListView);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pushList = new ArrayList<Push>();
        pushAdapter = new PushAdapter(inflater,pushList);
        pushListView.setAdapter(pushAdapter);
        pushListView.setOnItemClickListener(new ItemClickHandler());
        reflashList();
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.alertdialog_push, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.addpush));
                builder.setView(v);
                final EditText editText = v.findViewById(R.id.contentEditText);
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newPush = new Push("",userInfo.getStore().getID(),editText.getText().toString(),"0","");
                        Thread t = new Thread(new AddPush());
                        progressDialog = ProgressDialog.show(context, "請稍等...", "資料新增中...", true);
                        t.start();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    class ItemClickHandler implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
            String[] item;
            Thread t;
            switch (pushList.get((int)l).getState()){
                case "0":
                    item = getResources().getStringArray(R.array.storePushArray0);
                    new AlertDialog.Builder(context)
                            .setItems(item, new DialogInterface.OnClickListener() {
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
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    editPush = new Push(pushList.get((int)l).getID(),userInfo.getStore().getID(),editText.getText().toString(),"0","");
                                                    Thread t = new Thread(new UpdatePush());
                                                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                                    t.start();
                                                    dialog.dismiss();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            break;
                                        case 1://刪除推播
                                            editPush = new Push(pushList.get((int)l).getID(),"","","","");
                                            Thread t1 = new Thread(new DeletePush());
                                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                            t1.start();
                                            dialog.dismiss();
                                            break;
                                        case 2://要求審核
                                            editPush = new Push(pushList.get((int)l).getID(),userInfo.getStore().getID(),pushList.get((int)l).getContent().toString(),"1","");
                                            Thread t2 = new Thread(new UpdatePush());
                                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                            t2.start();
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .show();
                    break;
                case "1":
                    item = getResources().getStringArray(R.array.storePushArray1);
                    new AlertDialog.Builder(context)
                            .setItems(item, new DialogInterface.OnClickListener() {
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
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    editPush = new Push(pushList.get((int)l).getID(),userInfo.getStore().getID(),editText.getText().toString(),"1","");
                                                    Thread t = new Thread(new UpdatePush());
                                                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                                    t.start();
                                                    dialog.dismiss();
                                                }
                                            });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                            break;
                                        case 1://刪除推播
                                            editPush = new Push(pushList.get((int)l).getID(),"","","","");
                                            Thread t1 = new Thread(new DeletePush());
                                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                            t1.start();
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .show();
                    break;
                case "2":
                    item = getResources().getStringArray(R.array.storePushArray2);
                    new AlertDialog.Builder(context)
                            .setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0://編輯推播
                                            new AlertDialog.Builder(context)
                                                    .setMessage(R.string.editPushNeedRe)
                                                    .setPositiveButton(R.string.check, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            LayoutInflater inflater = LayoutInflater.from(context);
                                                            View v = inflater.inflate(R.layout.alertdialog_push, null);
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                            builder.setTitle(getResources().getString(R.string.editPush));
                                                            builder.setView(v);
                                                            final EditText editText = v.findViewById(R.id.contentEditText);
                                                            editText.setText(pushList.get((int) l).getContent());
                                                            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            builder.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    editPush = new Push(pushList.get((int) l).getID(), userInfo.getStore().getID(), editText.getText().toString(), "1", "");
                                                                    Thread t = new Thread(new UpdatePush());
                                                                    progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                                                    t.start();
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            AlertDialog alertDialog = builder.create();
                                                            alertDialog.show();
                                                        }
                                                    })
                                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            return;
                                                        }
                                                    })
                                                    .show();

                                            break;
                                        case 1://刪除推播
                                            editPush = new Push(pushList.get((int) l).getID(), "", "", "", "");
                                            Thread t1 = new Thread(new DeletePush());
                                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                            t1.start();
                                            dialog.dismiss();
                                            break;
                                        case 2://發送推播
                                            int point = Integer.parseInt(userInfo.getStore().getPoint());
                                            if (point < 10) {
                                                Toast.makeText(context, getResources().getString(R.string.noPoint), Toast.LENGTH_SHORT).show();
                                            } else {
                                                editPush = new Push(pushList.get((int) l).getID(), userInfo.getStore().getID(), pushList.get((int) l).getContent().toString(), "3", "");
                                                Thread t2 = new Thread(new UpdatePush());
                                                Thread t3 = new Thread(new SendPush());
                                                progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                                t2.start();
                                                t3.start();
                                            }
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .show();
                    break;
                case "3":
                    item = getResources().getStringArray(R.array.storePushArray3);
                    new AlertDialog.Builder(context)
                            .setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case 0://刪除推播
                                            editPush = new Push(pushList.get((int)l).getID(),"","","","");
                                            Thread t1 = new Thread(new DeletePush());
                                            progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
                                            t1.start();
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .show();
                    break;
            }

        }
    }

    class GetPush implements Runnable{
        @Override
        public void run() {
            Push[] push = database.GetPush(userInfo.getStore().getID(),"%");
            if(push.length !=0){
                for(int i=0;i<push.length;i++) {
                    pushList.add(push[i]);
                }
            }
            mainThreadHandler.sendEmptyMessage(GET_SUCCESS);
        }
    }
    class DeletePush implements Runnable{
        @Override
        public void run() {
            if(database.DeletePush(editPush.getID()).equals("Successful.")){
                mainThreadHandler.sendEmptyMessage(EDIT_SUCCESS);
            }else{
                mainThreadHandler.sendEmptyMessage(ADD_FAIL);
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

    class SendPush implements Runnable{
        @Override
        public void run() {
            database.SendPush(editPush);
            int point = Integer.parseInt(userInfo.getStore().getPoint()) - 10;
            userInfo.getStore().putPoint(Integer.toString(point));
            database.UpdatePoint(userInfo.getStore());
        }
    }
    class AddPush implements Runnable{
        @Override
        public void run() {
            if(database.AddPush(newPush).equals("Successful.")){
                mainThreadHandler.sendEmptyMessage(ADD_SUCCESS);
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
                    pushAdapter.notifyDataSetChanged();
                    break;
                case ADD_SUCCESS:
                    progressDialog.dismiss();
                    reflashList();
                    pushAdapter.notifyDataSetChanged();
                    break;
                case ADD_FAIL:
                    progressDialog.dismiss();
                    Toast.makeText(context, getResources().getString(R.string.addFail), Toast.LENGTH_SHORT).show();
                    break;
                case GET_SUCCESS:
                    progressDialog.dismiss();
                    pushAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    void reflashList(){
        pushList.clear();
        database.refreshStoreIndex();
        progressDialog = ProgressDialog.show(context, "請稍等...", "資料更新中...", true);
        Thread t = new Thread(new GetPush());
        t.start();
    }

}
class PushAdapter extends BaseAdapter {
    private ArrayList<Push> push;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        RelativeLayout relativeLayout;
        TextView contentTextView;
        TextView stateTextView;
    }

    public PushAdapter(LayoutInflater inflater, ArrayList<Push> push) {
        this.push = push;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return push.size();
    }

    @Override
    public Push getItem(int i) {
        return push.get(i);
    }

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
        viewHolder.contentTextView.setText(getItem(position).getContent());
        switch (getItem(position).getState()){//0:未審核，1:待審核，2:已審核，3:已發送
            case "0":
                viewHolder.stateTextView.setText("未審核");
                viewHolder.contentTextView.setTextColor(android.graphics.Color.BLACK);
                viewHolder.stateTextView.setTextColor(android.graphics.Color.BLACK);
                break;
            case "1":
                viewHolder.stateTextView.setText("待審核");
                viewHolder.contentTextView.setTextColor(android.graphics.Color.RED);
                viewHolder.stateTextView.setTextColor(android.graphics.Color.RED);
                break;
            case "2":
                viewHolder.stateTextView.setText("已審核");
                viewHolder.contentTextView.setTextColor(android.graphics.Color.BLUE);
                viewHolder.stateTextView.setTextColor(android.graphics.Color.BLUE);
                break;
            case "3":
                viewHolder.stateTextView.setText("已發送");
                viewHolder.contentTextView.setTextColor(android.graphics.Color.GRAY);
                viewHolder.stateTextView.setTextColor(android.graphics.Color.GRAY);
                break;
        }

        return view;
    }
}