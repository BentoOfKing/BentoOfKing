package com.cce.nkfust.tw.bentoofking;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CommentListViewRecyclerAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private ArrayList<Comment> elementData;
    private OnItemClickListener myOnItemClickListener = null;
    private String storeName;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout commentListViewLayout;
        ImageView[] star;
        TextView replyTitle;
        TextView storeReply;
        TextView commentMember;
        TextView commentDate;
        TextView commentText;

        public ViewHolder(View itemView) {
            super(itemView);
            star = new ImageView[5];
            star[0] = itemView.findViewById(R.id.star1);
            star[1] = itemView.findViewById(R.id.star2);
            star[2] = itemView.findViewById(R.id.star3);
            star[3] = itemView.findViewById(R.id.star4);
            star[4] = itemView.findViewById(R.id.star5);
            commentMember = itemView.findViewById(R.id.commentMember);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentText = itemView.findViewById(R.id.commentText);
            replyTitle = itemView.findViewById(R.id.replyTitle);
            storeReply = itemView.findViewById(R.id.storeReply);
            commentListViewLayout = itemView.findViewById(R.id.border);
        }
    }

    public CommentListViewRecyclerAdapter(ArrayList<Comment> dataset, String storeName){
        elementData = dataset;
        this.storeName = storeName;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.myOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_listitemnew, parent, false);
        v.setOnClickListener(this);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder)viewHolder;
        viewHolder.itemView.setTag(position);
        holder.commentMember.setText(elementData.get(position).getMemberNickName());
        holder.commentDate.setText(elementData.get(position).getContentTime());
        holder.commentText.setText(elementData.get(position).getNote());
        holder.replyTitle.setText(storeName);
        if (elementData.get(position).getReply().equals("")) {
            holder.replyTitle.setVisibility(View.INVISIBLE);
            holder.storeReply.setText("");
        }
        else {
            holder.replyTitle.setVisibility(View.VISIBLE);
            holder.storeReply.setText(elementData.get(position).getReply());
        }

        int score = Integer.valueOf(elementData.get(position).getScore());
        for(int i=0;i<5;i++) {
            if(i<score)
                holder.star[i].setImageResource(R.drawable.star_on);
            else
                holder.star[i].setImageResource(R.drawable.star_off);
        }
    }

    @Override
    public int getItemCount() {
        return elementData.size();
    }


    @Override
    public void onClick(View v){
        if(myOnItemClickListener != null)
            myOnItemClickListener.onItemClick(v,(int)v.getTag());
    }

}
