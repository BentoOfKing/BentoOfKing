package com.cce.nkfust.tw.bentoofking;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CommentListViewBaseAdapter extends BaseAdapter {
    private ArrayList<Comment> elementData;
    private LayoutInflater inflater;
    private Context context;

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
            convertView = this.inflater.inflate(R.layout.comment_listitem,null);
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
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.commentMember.setText(getItem(position).getMemberNickName());
        viewHolder.commentDate.setText(getItem(position).getContentTime());
        viewHolder.commentText.setText(getItem(position).getNote());



        return convertView;
    }




}
