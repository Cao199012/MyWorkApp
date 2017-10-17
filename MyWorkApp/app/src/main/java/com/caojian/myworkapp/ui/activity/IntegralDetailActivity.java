package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.model.data.DetailItem;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IntegralDetailActivity extends BaseTitleActivity {
    public static void go2IntegralDetailActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,IntegralDetailActivity.class);
        fromClass.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recy_integral_detail)
    RecyclerView mRecy_detail;

    Unbinder unbinder;
    List<DetailItem> mListData = new LinkedList<>();
    IntergralDetailAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_detail);

        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("积分明细");
        initRecy();
    }

    private void initRecy() {
        for (int i = 0; i < 5; i++)
        {
            DetailItem item = new DetailItem();
            mListData.add(item);
        }
        mRecy_detail.addItemDecoration(new LineDecoration(IntegralDetailActivity.this));
        mRecy_detail.setLayoutManager(new LinearLayoutManager(IntegralDetailActivity.this));
        mAdapter = new IntergralDetailAdapter();

        mRecy_detail.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    class IntergralDetailAdapter extends RecyclerView.Adapter<IntergralDetailAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.intergral_detail_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DetailItem item = mListData.get(position);
            // TODO: 2017/9/17 展示信息
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_money)
            TextView mTv_money;
            @BindView(R.id.tv_from)
            TextView mTv_from;
            @BindView(R.id.tv_time)
            TextView mTv_time;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
    }
}
