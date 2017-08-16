package com.caojian.myworkapp.mvp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.MvpBaseFragment;
import com.caojian.myworkapp.recy.OneRecyAdapter;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by CJ on 2017/7/20.
 */

public class MvpFragment extends MvpBaseFragment<MvpContract.View,MvpPresenter> implements MvpContract.View {

    @BindView(R.id.mvp_recy)
    RecyclerView myRecy;
    private Unbinder mUnbinder;
    private MvpPresenter mvpPresenter;
    private OneRecyAdapter myAdapter;
    private List<MvpItem> mList;
    public static MvpFragment getInstance(){
        return  new MvpFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nvp_fragment,container,false);
        mUnbinder =  ButterKnife.bind(this,root);

        initRecy();
        return root;
    }

    private void initRecy() {
        mList = new LinkedList<>();
        myAdapter = new OneRecyAdapter(mList);
        myRecy.setLayoutManager(new GridLayoutManager(getActivity().getBaseContext(),6, GridLayoutManager.VERTICAL,false));
       myRecy.addItemDecoration(new RecyclerView.ItemDecoration() {
           @Override
           public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
               super.getItemOffsets(outRect, view, parent, state);
               outRect.bottom = 10;
           }

           @Override
           public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

               int count = parent.getChildCount();
               float left = parent.getPaddingLeft();
               Paint dividerPaint = new Paint();
               dividerPaint.setColor(Color.BLACK);
               float right = parent.getWidth() - parent.getPaddingRight();
               for (int i = 0; i < count - 1; i++) {
                   View view = parent.getChildAt(i);
                   float top = view.getBottom();
                   float bottom = view.getBottom() + 10;
                   c.drawRect(left, top, right, bottom, dividerPaint);
               }

           }
       });
        myRecy.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpPresenter.requestRefresh();
    }

    @Override
    protected MvpPresenter createPresenter() {
        mvpPresenter = new MvpPresenter( new MvpModel(""),this);

        return mvpPresenter;
    }

    @Override
    public void disLoadingView() {

    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void showErrorView(String msg) {

    }

    @Override
    public void refreshContentView(List<MvpItem> pList) {
        mList.clear();
        if (pList != null && pList.size() > 0){
            mList.addAll(pList);
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreContentView() {

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
