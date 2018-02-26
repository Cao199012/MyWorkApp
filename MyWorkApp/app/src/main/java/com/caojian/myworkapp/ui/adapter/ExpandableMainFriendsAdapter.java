package com.caojian.myworkapp.ui.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CJ on 2017/11/25.
 */

public class ExpandableMainFriendsAdapter extends BaseExpandableListAdapter {

    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mList;
    ItemSelect mSelectListen;
    public ExpandableMainFriendsAdapter(List<FriendsAndGroupsMsg.DataBean.GroupsBean> pList)
    {
        mList = pList;
    }


    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getFriends().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getFriends().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandableGroupViewHold viewHold;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recy,parent,false);
            viewHold = new ExpandableGroupViewHold(convertView);
            convertView.setTag(viewHold);
        }else
        {
            viewHold = (ExpandableGroupViewHold) convertView.getTag();
        }
        FriendsAndGroupsMsg.DataBean.GroupsBean groupsBean = mList.get(groupPosition);
        viewHold.tv_name.setText(groupsBean.getGroupName());
        if(groupsBean.getIsCheckBeforeExit() == null || groupsBean.getIsCheckBeforeExit() == 2){
            viewHold.checkBox.setChecked(false);
        }else {
            viewHold.checkBox.setChecked(true);
            //初始化 添加好友‘
            mSelectListen.groupSelect(groupPosition,true);
        }

        if(groupPosition+1 >= mList.size()){  //好友组没有勾选框
            viewHold.checkBox.setVisibility(View.INVISIBLE);
        }else {
            viewHold.checkBox.setVisibility(View.VISIBLE);

        }
        viewHold.checkBox.setFocusable(false);
        viewHold.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSelectListen.groupSelect(groupPosition,isChecked);
            }
        });
        viewHold.tv_name.setText(groupsBean.getGroupName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableChildViewHold viewHold;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.friend_select_item, parent, false);
            viewHold = new ExpandableChildViewHold(convertView);
            convertView.setTag(viewHold);
        }else
        {
            viewHold = (ExpandableChildViewHold) convertView.getTag();
        }
        FriendDetailInfo.DataBean dataBean = mList.get(groupPosition).getFriends().get(childPosition);
        viewHold.mIdView.setText(dataBean.getFriendRemarkName());
        //是否监测，
        if(dataBean.getIsCheckBeforeExit() == null || dataBean.getIsCheckBeforeExit() == 2){
            viewHold.radioButton.setChecked(false);
        }else {
            viewHold.radioButton.setChecked(true);
            //初始化 添加好友‘
            mSelectListen.childSelect(groupPosition,childPosition,true);

        }
        viewHold.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectListen.childSelect(groupPosition,childPosition, viewHold.radioButton.isChecked());
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ExpandableGroupViewHold {
        @BindView(R.id.group_select)
        AppCompatCheckBox checkBox;
        @BindView(R.id.group_name)
        TextView tv_name;
        public ExpandableGroupViewHold(View itemView) {

            ButterKnife.bind(this,itemView);
        }
    }

    public class ExpandableChildViewHold {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final AppCompatCheckBox radioButton;
        public FriendDetailInfo.DataBean mItem;

        public ExpandableChildViewHold(View view) {
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            radioButton = (AppCompatCheckBox) view.findViewById(R.id.rad_select);
        }
    }
    public void setGroupSelect(ItemSelect mItemSelect) {
        mSelectListen = mItemSelect;
    }

    public interface ItemSelect {
        boolean groupSelect(int groupPosition, boolean isChecked);
        boolean childSelect(int groupPosition,int childPosition, boolean isChecked);
    }
}
