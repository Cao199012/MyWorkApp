package com.caojian.myworkapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.caojian.myworkapp.model.data.FriendItem;

import java.util.List;

/**
 * Created by CJ on 2017/7/31.
 */

public class FriendRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FriendItem> mDataList;

    public FriendRecyAdapter(List<FriendItem> pList){
        mDataList = pList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private class FriendViewHolid extends RecyclerView.ViewHolder{

        public FriendViewHolid(View itemView) {
            super(itemView);
        }
    }
}
