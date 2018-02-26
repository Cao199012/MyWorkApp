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
import com.bumptech.glide.Glide;
import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.LocationItem;
import com.caojian.myworkapp.model.response.FriendDetailInfo;
import com.caojian.myworkapp.model.response.FriendsAndGroupsMsg;
import com.caojian.myworkapp.ui.activity.SelectPointActivity;
import com.caojian.myworkapp.until.Until;
import com.caojian.myworkapp.widget.ImageLoad;

import java.io.File;
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

        FriendDetailInfo.DataBean dataBean = mFriendData.get(groupPosition).getFriends().get(childPosition);
        childViewHolder.mTv_name.setText(dataBean.getFriendRemarkName());
        if(!dataBean.getHeadPic().isEmpty()){
            File _file = ImageLoad.getBitmapFile(dataBean.getHeadPic());
            if(_file != null) {
                Glide.with(parent.getContext()).load(_file).into(childViewHolder.img_head);
            }else {
                Glide.with(parent.getContext()).load(Until.HTTP_BASE_IMAGE_URL+dataBean.getHeadPic()).into(childViewHolder.img_head);
            }
        }


        LocationItem  item = null;
        if(_list != null && !_list.isEmpty()) {
            for (LocationItem _item : _list) {
                if (_item.getPhoneNo().equals(dataBean.getFriendPhoneNo())) {
                    item = _item;
                }
            }

        }
        LocationItem finalItem = item;

        childViewHolder.mItem_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(finalItem);
            }
        });
        if(item == null)
        {
            childViewHolder.mTv_dec.setText("好友暂无坐标");
        } else if(item.getLatitude().isEmpty() || item.getLongitude().isEmpty())
        {
            childViewHolder.mTv_dec.setText("未得到好友授权");
        }else
        {
            LatLng ll = new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude()));
            GeoCoder geoCoder = GeoCoder.newInstance();
            // 设置地理编码检索监听者
            geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                // 反地理编码查询结果回调函数
                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null
                            || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        // 没有检测到结果
                        childViewHolder.mTv_dec.setText("地理位置解析失败");
                        geoCoder.destroy();
                        return;
                    }
                    childViewHolder.mTv_dec.setText(result.getSematicDescription());
                    geoCoder.destroy();
                }
                // 地理编码查询结果回调函数
                @Override
                public void onGetGeoCodeResult(GeoCodeResult result) {
                }
            });
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



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
    }
}
