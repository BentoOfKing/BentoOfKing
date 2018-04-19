package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;

import java.util.ArrayList;

public class EditDataBaseCommentDialog extends Activity {
    private static String passUserInfo = "USER_INFO";
    private UserInfo userInfo;
    private Comment selectComment;
    private Intent infoIntent;
    DialogListViewBaseAdapter adapter;
    ArrayList<String> actionList;
    ListView actionListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_63_data_base_comment_dialog);
        getIntentInfo();

        actionListView = findViewById(R.id.editCommentListView);
        UpdateActionList();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adapter = new DialogListViewBaseAdapter(actionList,inflater);
        actionListView.setAdapter(adapter);
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
}
