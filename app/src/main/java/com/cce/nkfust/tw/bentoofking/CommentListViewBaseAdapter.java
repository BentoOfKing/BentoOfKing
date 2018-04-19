package com.cce.nkfust.tw.bentoofking;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentListViewBaseAdapter extends BaseAdapter {
    private static final int DELETE_COMMENT = 64;
    private ArrayList<Comment> elementData;
    private LayoutInflater inflater;
    private Context context;
    private Database databaseForComment;
    private HandlerThread threadForDataBase;
    private Handler handlerForTFDB;
    public static class ViewHolder{
        ConstraintLayout commentListViewLayout;
        TextView commentMember;
        TextView commentDate;
        TextView commentText;
        Button editButton;
        Button reportButton;
        Button deleteButton;
        Button replyButton;
    }

    public CommentListViewBaseAdapter(ArrayList<Comment> data, LayoutInflater inflater){
        this.elementData = data;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.elementData.size();
    }

    @Override
    public Comment getItem(int position) { return this.elementData.get(position); }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.item_comment_listitem,null);
            viewHolder.commentMember = convertView.findViewById(R.id.commentMember);
            viewHolder.commentDate = convertView.findViewById(R.id.commentDate);
            viewHolder.commentText = convertView.findViewById(R.id.commentText);
            viewHolder.deleteButton = convertView.findViewById(R.id.deleteButton);
            viewHolder.deleteButton.setEnabled(false);
            viewHolder.editButton = convertView.findViewById(R.id.editButton);
            viewHolder.editButton.setEnabled(false);
            viewHolder.replyButton = convertView.findViewById(R.id.replyButton);
            viewHolder.replyButton.setEnabled(false);
            viewHolder.reportButton = convertView.findViewById(R.id.reportButton);
            viewHolder.commentListViewLayout = convertView.findViewById(R.id.border);
            DeleteButtonListener listener = new DeleteButtonListener(getItem(position).getID());
            viewHolder.deleteButton.setOnClickListener(listener);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.commentMember.setText(getItem(position).getMemberNickName());
        viewHolder.commentDate.setText(getItem(position).getContentTime());
        viewHolder.commentText.setText(getItem(position).getNote());



        return convertView;
    }

    private class DeleteButtonListener implements View.OnClickListener{
        private String id;
        public DeleteButtonListener(String id){
            this.id = id;
        }
        @Override
        public void onClick(View view) {
            threadForDataBase = new HandlerThread("DeleteCommentFromDataBase");
            threadForDataBase.start();
            handlerForTFDB = new Handler(threadForDataBase.getLooper()){
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
            };
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("ID",this.id);
            msg.setData(data);
            msg.what = DELETE_COMMENT;
            handlerForTFDB.sendMessage(msg);
        }
    }








}
