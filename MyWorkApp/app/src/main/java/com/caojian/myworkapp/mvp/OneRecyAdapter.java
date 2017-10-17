package com.caojian.myworkapp.mvp;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.mvp.MvpItem;

import java.util.List;

/**
 * Created by CJ on 2017/7/21.
 */

public class OneRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_TYPE1 = 0xff01;
    private static final int ITEM_TYPE2 = 0xff02;
    private static final int ITEM_TYPE3 = 0xff03;

    private List<MvpItem> mDataList;

    public OneRecyAdapter(List<MvpItem> pList){
        mDataList = pList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType)
        {
            case ITEM_TYPE1:
                viewHolder = new ViewHolderOne(LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_item_one,parent,false));
                break;
            case ITEM_TYPE2:
                viewHolder = new ViewHolderTwo(LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_item_two,parent,false));
                break;
            case ITEM_TYPE3:
                viewHolder = new ViewHolderThree(LayoutInflater.from(parent.getContext()).inflate(R.layout.recy_item_three,parent,false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MvpItem item = mDataList.get(position);
        if(holder instanceof ViewHolderOne)
        {
            ((ViewHolderOne) holder).tv_one.setText(item.getMsg());
        }
        if(holder instanceof ViewHolderTwo)
        {
            ((ViewHolderTwo) holder).tv_two.setText(item.getMsg());
        }
        if(holder instanceof ViewHolderThree)
        {
            ((ViewHolderThree) holder).tv_three.setText(item.getMsg());
        }
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position))
                    {
                        case ITEM_TYPE1:
                            return gridLayoutManager.getSpanCount();
                        case ITEM_TYPE2:
                            return 3;
                        case ITEM_TYPE3:
                            return 2;
                        default:
                            return  gridLayoutManager.getSpanCount();
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
        {
            return ITEM_TYPE1;
        } else if(1<=position && position<=2)
        {
            return ITEM_TYPE2;
        } else if(position<=8)
        {
            return ITEM_TYPE3;
        }else
        {
            return ITEM_TYPE1;
        }
    }

    private class ViewHolderOne extends RecyclerView.ViewHolder{
        public TextView tv_one;
        public ViewHolderOne(View itemView) {
            super(itemView);
            tv_one = (TextView) itemView.findViewById(R.id.tv_one);
        }
    }
    private class ViewHolderTwo extends RecyclerView.ViewHolder{
        public TextView tv_two;
        public ViewHolderTwo(View itemView) {
            super(itemView);
            tv_two = (TextView) itemView.findViewById(R.id.tv_two);
        }
    }
    private class ViewHolderThree extends RecyclerView.ViewHolder{
        public TextView tv_three;
        public ViewHolderThree(View itemView) {
            super(itemView);
            tv_three = (TextView) itemView.findViewById(R.id.tv_three);
        }
    }
}
