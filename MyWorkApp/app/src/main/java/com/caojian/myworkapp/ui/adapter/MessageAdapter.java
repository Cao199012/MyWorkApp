package com.caojian.myworkapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.ApplyFriendsRecord;
import com.caojian.myworkapp.model.data.MessageItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/3.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private List<ApplyFriendsRecord.DataBean.RecordsBean> mListData;

    RequestMessageListener mListener;
    Context mContext;

    public MessageAdapter(List<ApplyFriendsRecord.DataBean.RecordsBean> pListData, RequestMessageListener listener, Context context) {
        mListData = pListData;
        mListener = listener;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_message_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ApplyFriendsRecord.DataBean.RecordsBean item = mListData.get(position);
        holder.tv_name.setText(item.getFriendNickName());
        holder.tv_desc.setText(item.getFriendPhoneNo());
        if(item.getIsApprove().equals("1")){
            holder.mBtn_accept.setText("已添加");
        }else {
            holder.mBtn_accept.setText("接受");
        }

        if(!item.getHeadPic().isEmpty()){
            Glide.with(mContext).load(item.getHeadPic()).into(holder.img_friend);
        }
        // TODO: 2017/9/3 操作显示信息
        holder.mBtn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.getIsApprove().equals("1")){
                    mListener.accept(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_frined)
        ImageView img_friend;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_desc)
        TextView tv_desc;
        @BindView(R.id.btn_accept)
        Button mBtn_accept;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    public interface RequestMessageListener{
        void accept(ApplyFriendsRecord.DataBean.RecordsBean item);
    }
}
