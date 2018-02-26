package com.caojian.myworkapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.ImageLoad;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/2.
 */

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.ViewHolder>{

    List<FriendDetailInfo.DataBean> mFriendData;
    ItemClick itemClick;
    Context mContext;
    public GroupMembersAdapter(List<FriendDetailInfo.DataBean> pList, ItemClick pItemClick, Context pContext)
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
            Glide.with(mContext).load(R.mipmap.ic_add_team_member).into(holder.img_head);
            holder.img_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.addItem();
                }
            });
            holder.item_name.setVisibility(View.GONE);

        } else if (position == mFriendData.size() + 1)
        {
            Glide.with(mContext).load(R.mipmap.ic_remove_team_member).into(holder.img_head);
            holder.img_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.removeItem();
                }
            });
            holder.item_name.setVisibility(View.GONE);
        }else {
            FriendDetailInfo.DataBean item = mFriendData.get(position);
            // TODO: 2017/9/9 设置其他信息
            holder.item_name.setVisibility(View.VISIBLE );
            holder.item_name.setText(item.getFriendRemarkName());
            if(!item.getHeadPic().equals("")){
                File _file = ImageLoad.getBitmapFile(item.getHeadPic());
                if(_file != null) {
                    Glide.with(mContext).load(_file).into(holder.img_head);
                }else {
                    Glide.with(mContext).load(Until.HTTP_BASE_IMAGE_URL+item.getHeadPic()).into(holder.img_head);
                }
            }else {

                Glide.with(mContext).load(R.mipmap.logo_launcher).into(holder.img_head);

            }
            holder.mItem_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击跳转到好友详情页面
                    itemClick.itemSelected(item);
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if(mFriendData.size() < 1) //没有好友时需要一个添加按钮
        {
            return mFriendData.size()+1;
        }else   //有好友时需要添加和删除两个按钮
        {
            return mFriendData.size() + 2;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_item)
        ImageView img_head;
        @BindView(R.id.item_name)
        TextView item_name;
        @BindView(R.id.body_item)
        LinearLayout mItem_body;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemClick{
        void itemSelected(FriendDetailInfo.DataBean item);

        void addItem();
        void removeItem();

    }
}
