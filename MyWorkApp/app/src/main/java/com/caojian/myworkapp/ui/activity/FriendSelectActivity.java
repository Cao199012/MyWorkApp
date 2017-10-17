package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.adapter.MyItemRecyclerViewAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.fragment.FriendSelectFragment;
import com.caojian.myworkapp.model.data.DummyContent;
import com.caojian.myworkapp.widget.SectionDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FriendSelectActivity extends BaseTitleActivity implements FriendSelectFragment.OnListFragmentInteractionListener{

    public static void go2FriendSelectActivity(Context from,int requestCode,String title){
        Intent intent = new Intent(from,FriendSelectActivity.class);
        intent.putExtra("requestCode",requestCode);
        intent.putExtra("title",title);
        ((Activity)from).startActivityForResult(intent,requestCode);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mContainer;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private Unbinder unbinder;
    public static final int SELECT_MIAN = 100;
    public static final int SELECT_ADD = 101;
    public static final int SELECT_DELETE = 102;
    public static final int SELECT_RAIL = 103;
    public static final int SELECT_TRACK = 104;
    public int selectKind = SELECT_MIAN;//0首页选择显示好友 1群组添加或删除好友 2选择监视好友 3查看好友轨迹
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_select);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("选择好友");
       // ActivityUntil.initActionBar(toolbar,FriendSelectActivity.this,R.drawable.ic_arrow_back);

        selectKind = getIntent().getIntExtra("requestCode",SELECT_MIAN);

//        FriendSelectFragment friendSelectFragment = (FriendSelectFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        if(friendSelectFragment == null)
//        {
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, FriendSelectFragment.newInstance(1)).commit();
//        }

        initRecy();
    }

    private void initRecy() {


        recyclerView.setLayoutManager(new LinearLayoutManager(FriendSelectActivity.this));
        // TODO: 2017/10/8 根据不同的访问页面显示不同的数据
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, this));
        if(selectKind == SELECT_MIAN){
            recyclerView.addItemDecoration(new SectionDecoration(FriendSelectActivity.this) {
                @Override
                public String getGroupId(int position) {
                    return DummyContent.ITEMS.get(position).id.substring(0,1).toUpperCase();
                }
            });
        }

    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == android.R.id.home)
//        {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        finish();
        //放弃
        setResult(RESULT_CANCELED);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        if(item.isChecked)
        {
            dummyItemList.add(item);
        }else
        {
            dummyItemList.remove(item);
        }
    }

    public void selectDone(View v)
    {
        //for ()
        Intent intent = new Intent();
        intent.putExtra("num",dummyItemList.size());
        setResult(RESULT_OK,intent);
        finish();
    }
}
