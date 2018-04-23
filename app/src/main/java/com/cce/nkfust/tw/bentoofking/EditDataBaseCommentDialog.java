package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class EditDataBaseCommentDialog extends Activity {
    private static final int DELETE_COMMENT = 64;
    private static final int EDIT_FINISHED = 65;
    private static String passUserInfo = "USER_INFO";
    private Database databaseForComment;
    private UserInfo userInfo;
    private Comment selectComment;
    private Intent infoIntent;
    private MainThreadHandler mainThreadHandler;
    private HandlerThread editCommentFromDataBase;
    private EditCommentThreadHandler EDCFDBHandler;
    private Context context;
    DialogListViewBaseAdapter adapter;
    ArrayList<String> actionList;
    ListView actionListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_63_data_base_comment_dialog);
        mainThreadHandler = new MainThreadHandler(Looper.getMainLooper());
        getIntentInfo();
        editCommentFromDataBase = new HandlerThread("EditComment");
        editCommentFromDataBase.start();
        EDCFDBHandler = new EditCommentThreadHandler(editCommentFromDataBase.getLooper());
        actionListView = findViewById(R.id.editCommentListView);
        UpdateActionList();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new DialogListViewBaseAdapter(actionList,inflater);
        actionListView.setAdapter(adapter);
        ActionItemPressed itemListener = new ActionItemPressed();
        actionListView.setOnItemClickListener(itemListener);
    }

    private void UpdateActionList(){
        actionList = new ArrayList<String>();
        actionList.add("刪除");
        actionList.add("編輯");
        actionList.add("檢舉");
        actionList.add("回覆");
    }
    private void getIntentInfo(){
        infoIntent = getIntent();
        userInfo = (UserInfo) infoIntent.getExtras().getSerializable(passUserInfo);
        selectComment = (Comment) infoIntent.getExtras().getSerializable("SelectComment");
        if(userInfo == null) userInfo = new UserInfo();
    }

    private class ActionItemPressed implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(actionList.get(position).equals("刪除")){
                Message msg = new Message();
                Bundle bag = new Bundle();
                String commentID = selectComment.getID();
                bag.putString("ID",commentID);
                msg.setData(bag);
                msg.what = DELETE_COMMENT;
                EDCFDBHandler.sendMessage(msg);
            }
            mainThreadHandler.sendEmptyMessage(EDIT_FINISHED);
        }
    }

    private class EditCommentThreadHandler extends Handler{
        public EditCommentThreadHandler(){
            super();
        }
        public EditCommentThreadHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case DELETE_COMMENT:
                    databaseForComment = new Database();
                    databaseForComment.deleteComment(msg.getData().getString("ID"));
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class MainThreadHandler extends Handler{
        public MainThreadHandler(){
            super();
        }
        public MainThreadHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case EDIT_FINISHED:
                    setResult(RESULT_OK);
                    EditDataBaseCommentDialog.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
