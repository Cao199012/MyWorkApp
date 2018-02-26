package com.caojian.myworkapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.AddByPhoneContrct;
import com.caojian.myworkapp.ui.contract.AddGroupContract;
import com.caojian.myworkapp.ui.presenter.AddGroupPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GroupCreateActivity extends MvpBaseActivity<AddGroupContract.View,AddGroupPresenter> implements AddGroupContract.View {

    public static void go2GroupCreateActivity(Context from) {
        Intent intent = new Intent(from, GroupCreateActivity.class);
        ((Activity) from).startActivityForResult(intent, 102);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_name)
    EditText mEdit_name;
    private Unbinder unbinder;
    AddGroupPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        unbinder = ButterKnife.bind(this);
        //设置标题名
        toolbar.setTitle("新建好友群");
    }


    public void createGroup(View view)
    {
        String name = mEdit_name.getText().toString().trim();
        if(name.isEmpty())
        {
            showToast("组名不能为空",Toast.LENGTH_SHORT);
            return;
        }
        mPresenter.addGroup(name);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public AddGroupPresenter createPresenter() {
        mPresenter = new AddGroupPresenter(GroupCreateActivity.this,this);
        return mPresenter;
    }

    @Override
    public void addSuccess(String msg) {
        // TODO: 2017/11/4 返回列表并更新列表
       setResult(RESULT_OK);
        finish();
    }

    @Override
    public void addError(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }
}
