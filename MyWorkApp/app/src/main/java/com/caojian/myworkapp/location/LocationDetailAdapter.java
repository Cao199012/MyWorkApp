package com.caojian.myworkapp.location;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.friend.dummy.FriendItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/2.
 */

public class LocationDetailAdapter extends RecyclerView.Adapter<LocationDetailAdapter.ViewHolder>{

    List<LocationItem> mFriendData;
    ItemClick itemClick;
    public LocationDetailAdapter(List<LocationItem> pList, ItemClick pItemClick)
    {
        mFriendData = pList;
        itemClick = pItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_detail_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LocationItem item = mFriendData.get(position);
        holder.mItem_body.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClick.itemSlect(item);
                return true;
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
        void itemSlect(LocationItem item);
    }
}
