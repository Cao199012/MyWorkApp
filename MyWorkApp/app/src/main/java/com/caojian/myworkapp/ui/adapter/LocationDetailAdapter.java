package com.caojian.myworkapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.activity.SelectPointActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/2.
 */

public class LocationDetailAdapter extends BaseExpandableListAdapter{

    List<FriendsAndGroupsMsg.DataBean.GroupsBean> mFriendData;
    ItemClick itemClick;
    public LocationDetailAdapter(List<FriendsAndGroupsMsg.DataBean.GroupsBean> pList, ItemClick pItemClick)
    {
        mFriendData = pList;
        itemClick = pItemClick;
    }

    @Override
    public int getGroupCount() {
        return mFriendData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFriendData.get(groupPosition).getFriends().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mFriendData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mFriendData.get(groupPosition).getFriends().get(childPosition);
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
         GroupViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gtoup_item,parent,false);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        FriendsAndGroupsMsg.DataBean.GroupsBean groupsBean = mFriendData.get(groupPosition);
        viewHolder.mTv_name.setText(groupsBean.getGroupName());
//        viewHolder.mTv_name.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                itemClick.groupSelect(groupsBean);
//                return true;
//            }
//        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_detail_item,parent,false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }else
        {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        List<LocationItem> _list = ((MyApplication)parent.getContext().getApplicationContext()).getmSeeFriendLocation();
        LocationItem item = null;
        if(groupPosition*childPosition+childPosition < _list.size()){
            item = _list.get(groupPosition*childPosition+childPosition);
            childViewHolder.mTv_name.setText(item.getPhoneNo());
            if(item.getLatitude().isEmpty() || item.getLongitude().isEmpty())
            {

            }else
            {
                LatLng ll = new LatLng(Long.parseLong(item.getLatitude()),Long.parseLong(item.getLongitude()));
                GeoCoder geoCoder = GeoCoder.newInstance();
                // 设置地理编码检索监听者
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    // 反地理编码查询结果回调函数
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        if (result == null
                                || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            // 没有检测到结果
                            return;
                        }
                        childViewHolder.mTv_dec.setText(result.getAddress());
                        geoCoder.destroy();
                    }
                    // 地理编码查询结果回调函数
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult result) {
                        geoCoder.destroy();
                    }
                });
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            }
        }

        LocationItem finalItem = item;
        childViewHolder.mItem_body.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClick.itemSelect(finalItem);
                return true;
            }

        });
        childViewHolder.mItem_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(finalItem);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_detail_item,parent,false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        LocationItem item = mFriendData.get(position);
//        holder.mTv_name.setText(item.getPhoneNo());
//        holder.mItem_body.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                itemClick.itemSelect(item);
//                return true;
//            }
//
//        });
//        holder.mItem_body.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                itemClick.itemClick(item);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mFriendData.size();
//    }

    public class ChildViewHolder{
        @BindView(R.id.img_head)
        ImageView img_head;
        @BindView(R.id.tv_name)
        TextView mTv_name;
        @BindView(R.id.tv_dec)
        TextView mTv_dec;
        @BindView(R.id.friend_item_body)
        RelativeLayout mItem_body;
        public ChildViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }
    public class GroupViewHolder{
        @BindView(R.id.group_name)
        TextView mTv_name;
        @BindView(R.id.group_body)
        RelativeLayout mItem_body;
        public GroupViewHolder(View itemView) {
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemClick{
        void itemClick(LocationItem item);
        void itemSelect(LocationItem item);
        void groupSelect( FriendsAndGroupsMsg.DataBean.GroupsBean groupsBean);
    }
}
