package com.example.myapplicationtest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<Msg> mMsgList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        //ViewHolder通常出现在适配器里，为的是listview滚动的时候快速设置值，而不必每次都重新创建很多对象，从而提升性能。
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        //LayoutInflat.from()从一个Context中，获得一个布局填充器，这样你就可以使用这个填充器来把xml布局文件转为View对象了。
        //LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);这样的方法来加载布局msg_item.xml
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position){
        Msg msg =mMsgList.get(position);
        holder.centerlayout.setVisibility(View.GONE);
        if(msg.isNeedtimetype()){
            holder.centerlayout.setVisibility(View.VISIBLE);
            holder.centerMsg.setText(msg.getTime());
        }
        if(!msg.getWhocreate().equals(MainActivityforcharroom.username)){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.othersname.setText(msg.getWhocreate());
        }else if(msg.getWhocreate().equals(MainActivityforcharroom.username)){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
            holder.myname.setText(msg.getWhocreate());
        }
    }
    @Override
    public int getItemCount(){
        return mMsgList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        LinearLayout centerlayout;
        TextView leftMsg;
        TextView othersname;
        TextView myname;
        TextView rightMsg;
        TextView centerMsg;
        public ViewHolder(@NonNull View view){
            super(view);
            leftLayout = view.findViewById(R.id.left_layout);
            centerlayout = view.findViewById(R.id.center_layout);
            rightLayout = view.findViewById(R.id.right_layout);
            leftMsg = view.findViewById(R.id.left_msg);
            othersname = view.findViewById(R.id.others_name);
            myname = view.findViewById(R.id.my_name);
            rightMsg = view.findViewById(R.id.right_msg);
            centerMsg = view.findViewById(R.id.center_msg);
        }
    }
    public MsgAdapter (List<Msg> msgList){
        mMsgList = msgList;
    }

}
