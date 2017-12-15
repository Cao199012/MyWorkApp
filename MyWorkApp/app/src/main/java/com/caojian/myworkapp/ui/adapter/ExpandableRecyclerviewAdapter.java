package com.caojian.myworkapp.ui.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CJ on 2017/11/25.
 */

public class ExpandableRecyclerviewAdapter extends RecyclerView.Adapter<ExpandableRecyclerviewAdapter.ExpandableViewHold> {

    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mList;
    FriendSelectRecyclerViewAdapter mAdapter;
    FriendSelectRecyclerViewAdapter.SelectListen mSelectListen;
    GroupSelect mGroupSelect;
    public ExpandableRecyclerviewAdapter(List<FriendsAndGroupsMsg.DataBean.GroupsBean> pList,FriendSelectRecyclerViewAdapter.SelectListen pSelectListen)
    {
        mList = pList;
        mSelectListen = pSelectListen;
    }
    @Override
    public ExpandableViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recy,parent,false);
        return new ExpandableViewHold(itemView);
    }

    @Override
    public void onBindViewHolder(ExpandableViewHold holder, int position) {
        FriendsAndGroupsMsg.DataBean.GroupsBean bean = mList.get(position);
        holder.tv_name.setText(bean.getGroupName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mRecycler_expand.getVisibility() == View.VISIBLE){
                    holder.mRecycler_expand.setVisibility(View.GONE);
                    return;
                }
                holder.mRecycler_expand.setVisibility(View.VISIBLE);
            }
        });

        if(position == mList.size() - 1)
        {
            mAdapter = new FriendSelectRecyclerViewAdapter(mList.get(position).getFriends(),mSelectListen,2);
            holder.checkBox.setVisibility(View.GONE);
        }else
        {
            mAdapter = new FriendSelectRecyclerViewAdapter(mList.get(position).getFriends(),mSelectListen,1);
            holder.checkBox.setVisibility(View.VISIBLE);
            if(bean.getIsCheckBeforeExit()!=null&&bean.getIsCheckBeforeExit() == 1){
                holder.checkBox.setChecked(true);
                holder.checkBox.setEnabled(false);
            }else{
                holder.checkBox.setEnabled(true);
                holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!mGroupSelect.groupSelect(mList.get(position)))
                        {
                            buttonView.setChecked(false);
                        }
                    }
                });
            }
        }
        holder.mRecycler_expand.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ExpandableViewHold extends RecyclerView.ViewHolder{
        @BindView(R.id.group_select)
        AppCompatCheckBox checkBox;
        @BindView(R.id.group_name)
        TextView tv_name;
        @BindView(R.id.recycler_expand)
        RecyclerView mRecycler_expand;
        public ExpandableViewHold(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void setmGroupSelect(GroupSelect mGroupSelect) {
        this.mGroupSelect = mGroupSelect;
    }

    public interface GroupSelect{
        boolean groupSelect(FriendsAndGroupsMsg.DataBean.GroupsBean bean);
    }
}
