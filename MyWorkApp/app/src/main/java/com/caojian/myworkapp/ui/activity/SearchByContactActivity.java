package com.caojian.myworkapp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.ContactBean;
import com.caojian.myworkapp.ui.adapter.ContractAdapter;
import com.caojian.myworkapp.ui.base.MvpBaseActivity;
import com.caojian.myworkapp.ui.contract.AddByPhoneContrct;
import com.caojian.myworkapp.ui.presenter.AddByPhonePresenter;
import com.caojian.myworkapp.until.ActivityUntil;
import com.caojian.myworkapp.widget.LineDecoration;
import com.caojian.myworkapp.widget.MyDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchByContactActivity extends MvpBaseActivity<AddByPhoneContrct.View,AddByPhonePresenter> implements ContractAdapter.RequestMessageListener
        ,AddByPhoneContrct.View,MyDialogFragment.FragmentDialogListener {
    public static void go2SearchByContactActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,SearchByContactActivity.class);
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_contracts)
    RecyclerView mRecy_contracts;
    @BindView(R.id.contract_search)
    SearchView mContractsSearch;
    private Unbinder unbinder;
    private List<ContactBean> mListData = new ArrayList<>();
    private List<ContactBean> mALLData = new ArrayList<>();
    private ContractAdapter mListAdapter;
    AddByPhonePresenter mPresenter;
    MyDialogFragment mDialogFragment;
    String selectPhoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_contact);
        unbinder = ButterKnife.bind(this);
        toolbar.setTitle("联系人");
        //mContractsSearch.setAppSearchData();
        //搜索框输入按钮 list改变
        mContractsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContractsSearch.onActionViewExpanded();
            }
        });
        mContractsSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    setFilterText(newText);
                }else{
                    clearTextFilter();
                }
                return false;
            }
        });

        if(((MyApplication)getApplication()).getListData() != null && ((MyApplication)getApplication()).getListData().size() > 0) {
            mALLData = ((MyApplication)getApplication()).getListData();
            mListData.clear();
            mListData.addAll(mALLData);
            intRecy();
            return;
        }
        String[] permissions = ActivityUntil.myCheckPermission(SearchByContactActivity.this,new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS});
        if(permissions != null && permissions.length > 0)     //验证联系人权限、动态申请权限
        {
            ActivityCompat.requestPermissions(SearchByContactActivity.this,permissions,1);
        }else
        {
            testReadAllContacts();

        }
       // mRecy_contracts.setFilterTex
    }

    private void clearTextFilter() {  //清除联系人，展示所有联系人
        mListData.clear();
        mListData.addAll(mALLData);
        mListAdapter.notifyDataSetChanged();
    }

    private void setFilterText(String newText) { //搜索名词改变
        mListData.clear();
        for (ContactBean bean : mALLData){
            if(bean.getName().contains(newText) || bean.getName().equals(newText)){
                mListData.add(bean);
            }else {

            }
        }
        mListAdapter.notifyDataSetChanged();
    }



    //初始化list
    private void intRecy() {
        mListAdapter = new ContractAdapter(mListData,this);
        mRecy_contracts.setLayoutManager(new LinearLayoutManager(SearchByContactActivity.this));
        mRecy_contracts.addItemDecoration(new LineDecoration(SearchByContactActivity.this));
        mRecy_contracts.setAdapter(mListAdapter);
    }


    @Override
    public AddByPhonePresenter createPresenter() {
        mPresenter = new AddByPhonePresenter(SearchByContactActivity.this,this);
        return mPresenter;
    }

    /*
     * 读取联系人的信息
     */
    public void testReadAllContacts() {
        showProgress(SearchByContactActivity.this);
        mALLData.clear();
        mListData.clear(); //每次读取 列表和本地的都要清除缓存
        Observable observable = new Observable() {
            @Override
            protected void subscribeActual(Observer observer) {
                Cursor cursor = getBaseContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                int contactIdIndex = 0;
                int nameIndex = 0;
                if(cursor == null) return;
                if(cursor.getCount() > 0) {
                    contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                    nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                }
                while(cursor.moveToNext()) {
                    ContactBean bean = new ContactBean();
                    String contactId = cursor.getString(contactIdIndex);
                    String name = cursor.getString(nameIndex);
                    bean.setName(name);
            /*
             * 查找该联系人的phone信息
             */
                    Cursor phones = getBaseContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                            null, null);
                    int phoneIndex = 0;
                    if(phones == null) continue;
                    if(phones.getCount() > 0) {
                        phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    }
                    while(phones.moveToNext()) {
                        String phoneNumber = phones.getString(phoneIndex);
                        //Log.i(TAG, phoneNumber);
                        //
                        bean.setPhonenum(phoneNumber);
                    }
                    if(mALLData.contains(bean)) continue;
                    mALLData.add(bean);
                    observer.onNext(bean);
                }
                observer.onComplete();
            }
        };
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Observer<ContactBean>() {
                       @Override
                       public void onSubscribe(Disposable d) {

                       }

                       @Override
                       public void onNext(ContactBean value) {
                           mListData.add(value);
                       }

                       @Override
                       public void onError(Throwable e) {
                           hideProgress();
                           showToast("读取失败",Toast.LENGTH_SHORT);
                       }

                       @Override
                       public void onComplete() {
                           hideProgress();
                           ((MyApplication)getApplication()).setListData(mALLData);
                           intRecy();
                       }

                   });
    }

    @Override
    public void accept(ContactBean item) {
        selectPhoneNum = item.getPhonenum().replace("-","").trim();
        selectPhoneNum = selectPhoneNum.replace(" ","").trim();
       if(selectPhoneNum.equals(ActivityUntil.getPhone(SearchByContactActivity.this))){
            showToast("不能添加本人", Toast.LENGTH_SHORT);
            return;
        }
        mPresenter.checkPhone(selectPhoneNum);
    }
    public static final String TAG = "MainActivity";
    /* 联系人请求码 */
    private static final int REQUEST_CONTACTS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CONTACTS)
        {
            if(grantResults.length > 0)
            {
                for (int result : grantResults)
                {
                    if (result != PackageManager.PERMISSION_GRANTED)
                    {
                        showToast("必须同意所有权限才能使用", Toast.LENGTH_LONG);
                        return;
                    }
                }
                testReadAllContacts();
                intRecy();
            }

        }
    }

    @Override
    public void checkSuccess() {
//        if(mDialogFragment == null || !mDialogFragment.getTag().equals("check"))
//        {
//            mDialogFragment = MyDialogFragment.newInstance("验证结果","此用户已注册是否添加为好友","取消","确定");
//        }
//        mDialogFragment.show(getSupportFragmentManager(),"check");
        mPresenter.addFriend(selectPhoneNum,"请求添加");
    }

    @Override
    public void addSuccess(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
       // finish();
    }

    @Override
    public void error(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
    }

    @Override
    public void checkFail(String msg) {
        if(mDialogFragment == null)
        {
            mDialogFragment = MyDialogFragment.newInstance("邀请加入",msg,"取消","邀请");
        }
       // mDialogFragment.show(getSupportFragmentManager(),"Invite");
        ActivityUntil.showDialogFragment(getSupportFragmentManager(),mDialogFragment,"Invite");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void cancel() {
        mDialogFragment.dismiss();
    }

    @Override
    public void sure() {
        mDialogFragment.dismiss();
        shareApp(); //
    }
}
