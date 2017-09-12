package com.caojian.myworkapp.modules.friend.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.modules.friend.dummy.FriendItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/2.
 */

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>{

    List<FriendItem> mFriendData;
    ItemClick itemClick;
    Context mContext;
    public GroupMembersAdapter(List<FriendItem> pList, ItemClick pItemClick, Context pContext)
    {
        mFriendData = pList;
        itemClick = pItemClick;
        mContext = pContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_member_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        if (position == mFriendData.size())
        {
            Glide.with(mContext).load(R.mipmap.ic_juejin).into(holder.img_head);

        } else if (position == mFriendData.size() + 1)
        {
            Glide.with(mContext).load(R.mipmap.ic_action_camera).into(holder.img_head);
        }else {
            FriendItem item = mFriendData.get(position);
            // TODO: 2017/9/9 设置其他信息
            holder.mItem_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击跳转到好友详情页面
                    itemClick.itemSlect(item);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(mFriendData.size() < 1)
        {
            return 1;
        }else
        {
            return mFriendData.size() + 2;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_item)
        ImageView img_head;

        @BindView(R.id.body_item)
        LinearLayout mItem_body;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemClick{
        void itemSlect(FriendItem item);
    }
}
