package com.caojian.myworkapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/2.
 */

public class FriendGroupListAdapter extends RecyclerView.Adapter<FriendGroupListAdapter.ViewHolder>{

    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mFriendData;
    ItemClick itemClick;
    Context mContext;
    public FriendGroupListAdapter(List<FriendsAndGroupsMsg.DataBean.GroupsBean> pList, ItemClick pItemClick, Context pContext)
    {
        mFriendData = pList;
        itemClick = pItemClick;
        mContext = pContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendgroup_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FriendsAndGroupsMsg.DataBean.GroupsBean item = mFriendData.get(position);
        holder.mTv_name.setText(item.getGroupName());
        // TODO: 2017/9/9 设置其他信息
        holder.mItem_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到好友详情页面
                itemClick.itemSlect(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFriendData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_head)
        ImageView img_head;
        @BindView(R.id.tv_name)
        TextView mTv_name;
        @BindView(R.id.tv_dec)
        TextView mTv_dec;
        @BindView(R.id.friend_item_body)
        RelativeLayout mItem_body;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemClick{
        void itemSlect(FriendsAndGroupsMsg.DataBean.GroupsBean item);
    }
}
