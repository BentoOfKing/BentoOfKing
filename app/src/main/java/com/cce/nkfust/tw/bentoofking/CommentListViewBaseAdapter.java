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
        TextView commentScore;
        TextView replyTitle;
        TextView storeReply;
        TextView commentMember;
        TextView commentDate;
        TextView commentText;
    }

    public CommentListViewBaseAdapter(ArrayList<Comment> data, LayoutInflater inflater){
        this.elementData = data;
        this.inflater = inflater;
    }

    public void refresh(ArrayList<Comment> arrayList){
        elementData = arrayList;
        notifyDataSetChanged();
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
            viewHolder.commentScore = convertView.findViewById(R.id.ScoreTextView);
            viewHolder.replyTitle = convertView.findViewById(R.id.replyTitle);
            viewHolder.storeReply = convertView.findViewById(R.id.storeReply);
            viewHolder.commentListViewLayout = convertView.findViewById(R.id.border);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.commentMember.setText(getItem(position).getMemberNickName());
        viewHolder.commentDate.setText(getItem(position).getContentTime());
        viewHolder.commentText.setText(getItem(position).getNote());
        if (getItem(position).getReply().equals("")) {
            viewHolder.replyTitle.setVisibility(View.INVISIBLE);
            viewHolder.storeReply.setText("");
        }
        else {
            viewHolder.replyTitle.setVisibility(View.VISIBLE);
            viewHolder.storeReply.setText(getItem(position).getReply());
        }
        String star = "(";
        int score = Integer.valueOf(getItem(position).getScore());
        for(int i=0;i<score;i++)
            star+="☆";
        star+=")";
        viewHolder.commentScore.setText(star);



        return convertView;
    }









}
