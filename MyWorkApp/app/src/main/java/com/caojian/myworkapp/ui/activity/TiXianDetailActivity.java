package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.DrawInfo;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.TiXianDetailContract;
import com.caojian.myworkapp.ui.presenter.TiXianDetailPresenter;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TiXianDetailActivity extends MvpBaseActivity<TiXianDetailContract.View,TiXianDetailPresenter>implements TiXianDetailContract.View {
    public static void go2TiXianDetailActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,TiXianDetailActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recy_integral_detail)
    RecyclerView mRecy_detail;
    @BindView(R.id.swipe_integral)
    SwipeRefreshLayout mSwipeTiXian;
    Unbinder unbinder;
    List<DrawInfo.DetailsBean> mListData = new LinkedList<>();
    TiXianDetailAdapter mAdapter;
    TiXianDetailPresenter mPresenter;
    private static final String[] STATEMSG = {"待处理","提现成功","提现失败"};
    int nowPageNum = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_detail);
        unbinder = ButterKnife.bind(this);
        mToolbar.setTitle("提现记录");
        initRecy();
        mPresenter.getTiXianDetail(nowPageNum);
        mSwipeTiXian.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getTiXianDetail(nowPageNum);
            }
        });
    }

    private void initRecy() {
        mRecy_detail.addItemDecoration(new LineDecoration(TiXianDetailActivity.this));
        mRecy_detail.setLayoutManager(new LinearLayoutManager(TiXianDetailActivity.this));
        mAdapter = new TiXianDetailAdapter();
        mRecy_detail.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public TiXianDetailPresenter createPresenter() {
        mPresenter = new  TiXianDetailPresenter(this,TiXianDetailActivity.this);
        return mPresenter;
    }


    @Override
    public void getSuccess(DrawInfo.DataBean dataBean) {
        if(mSwipeTiXian.isRefreshing())
        {
            mSwipeTiXian.setRefreshing(false);
        }
        if(dataBean.getPageNumber() == nowPageNum)
        {
            mListData.clear();
        }
        nowPageNum = dataBean.getPageNumber();
        if(dataBean.getDetails() == null) return;
        mListData.addAll(dataBean.getDetails());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void error(String msg) {
        if(mSwipeTiXian.isRefreshing())
        {
            mSwipeTiXian.setRefreshing(false);
        }
        if(!msg.isEmpty()) showToast(msg, Toast.LENGTH_SHORT);
    }


    class TiXianDetailAdapter extends RecyclerView.Adapter<TiXianDetailAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tixian_detail_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DrawInfo.DetailsBean item = mListData.get(position);
            holder.mTv_money.setText(item.getRewardScore()+"");
            holder.mTv_from.setText(item.getBankNumber());
            holder.mTv_time.setText(item.getCreateTime());
            holder.mTv_state.setText(STATEMSG[item.getStatus()]);
            // TODO: 2017/9/17 展示信息
        }

        @Override
        public int getItemCount() {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.tv_money)
            TextView mTv_money;
            @BindView(R.id.tv_state)
            TextView mTv_state;
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
