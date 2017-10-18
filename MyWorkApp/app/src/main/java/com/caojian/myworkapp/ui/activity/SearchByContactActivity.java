package com.caojian.myworkapp.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.data.ContactBean;
import com.caojian.myworkapp.ui.adapter.ContractAdapter;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.ui.adapter.MessageAdapter;
import com.caojian.myworkapp.model.data.MessageItem;
import com.caojian.myworkapp.widget.LineDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchByContactActivity extends BaseTitleActivity implements ContractAdapter.RequestMessageListener {

    public static void go2SearchByContactActivity(Context fromClass)
    {
        Intent intent = new Intent(fromClass,SearchByContactActivity.class);
        // intent.pu
        fromClass.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recy_contracts)
    RecyclerView mRecy_contracts;

    private Unbinder unbinder;
    private List<ContactBean> mListData = new ArrayList<>();
    private ContractAdapter mListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_contact);
        unbinder = ButterKnife.bind(this);

        toolbar.setTitle("联系人");
        //验证联系人权限
        List<String> permission = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(SearchByContactActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.READ_CONTACTS);
        }
        if(ContextCompat.checkSelfPermission(SearchByContactActivity.this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.WRITE_CONTACTS);
        }

        if(!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);
            ActivityCompat.requestPermissions(SearchByContactActivity.this,permissions,1);
        }else
        {
            testReadAllContacts();
            intRecy();
        }

    }

    private void intRecy() {
        mListAdapter = new ContractAdapter(mListData,this);
        mRecy_contracts.setLayoutManager(new LinearLayoutManager(SearchByContactActivity.this));
        mRecy_contracts.addItemDecoration(new LineDecoration(SearchByContactActivity.this));
        mRecy_contracts.setAdapter(mListAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }



    /*
     * 读取联系人的信息
     */
    public void testReadAllContacts() {
        Cursor cursor = this.getBaseContext().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if(cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while(cursor.moveToNext()) {
            ContactBean bean = new ContactBean();
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            //
            bean.setName(name);
//            Log.i(TAG, contactId);
//            Log.i(TAG, name);

            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = this.getBaseContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if(phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while(phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                //Log.i(TAG, phoneNumber);
                //
                bean.setPhonenum(phoneNumber);
            }


            mListData.add(bean);
            /*
             * 查找该联系人的email信息
             */
//            Cursor emails = this.getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
//                    null, null);
//            int emailIndex = 0;
//            if(emails.getCount() > 0) {
//                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
//            }
//            while(emails.moveToNext()) {
//                String email = emails.getString(emailIndex);
//                Log.i(TAG, email);
//            }

        }
    }

    @Override
    public void accept(ContactBean item) {

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
}
