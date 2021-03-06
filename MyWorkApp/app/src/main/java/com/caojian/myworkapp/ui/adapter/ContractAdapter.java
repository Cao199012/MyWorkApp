package com.caojian.myworkapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.ContactBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caojian on 2017/9/3.
 */

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder>{

    private List<ContactBean> mListData;

    RequestMessageListener mListener;


    public ContractAdapter(List<ContactBean> pListData, RequestMessageListener listener) {
        mListData = pListData;
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_message_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactBean item = mListData.get(position);
        // TODO: 2017/9/3 操作显示信息
        holder.tv_name.setText(item.getName());
        holder.tv_desc.setText(item.getPhonenum());
        holder.img_friend.setVisibility(View.GONE);
        holder.mBtn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.accept(item);
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
        void accept(ContactBean item);
    }
}
