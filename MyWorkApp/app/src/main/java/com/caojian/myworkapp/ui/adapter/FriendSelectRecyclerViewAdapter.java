package com.caojian.myworkapp.ui.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.FriendDetailInfo;

import java.util.List;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class FriendSelectRecyclerViewAdapter extends RecyclerView.Adapter<FriendSelectRecyclerViewAdapter.ViewHolder> {

    private final List<FriendDetailInfo.DataBean> mValues;
    private SelectListen mListener;
    private int showType = 1 ; //1为好友群 2 为好友列表

    public FriendSelectRecyclerViewAdapter(List<FriendDetailInfo.DataBean> items, SelectListen listen,int type) {
        mValues = items;
        mListener = listen;
        showType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_select_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText( holder.mItem.getFriendPhoneNo());
        holder.mContentView.setText(holder.mItem.getFriendPhoneNo());
        if(showType == 1)
        {
            holder.radioButton.setVisibility(View.GONE);
        }else
        {
            holder.radioButton.setVisibility(View.VISIBLE);
            if(holder.mItem.getIsCheckBeforeExit() != null && holder.mItem.getIsCheckBeforeExit() == 1){
                holder.radioButton.setChecked(true);
                holder.radioButton.setEnabled(false);
            }else{
                holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!mListener.checkSelect(holder.mItem.getFriendPhoneNo())){
                            buttonView.setChecked(false);
                        }
                    }
                });
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final AppCompatCheckBox radioButton;
        public FriendDetailInfo.DataBean mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            radioButton = (AppCompatCheckBox) view.findViewById(R.id.rad_select);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface SelectListen{
        boolean checkSelect(String friendId);
    }

}
