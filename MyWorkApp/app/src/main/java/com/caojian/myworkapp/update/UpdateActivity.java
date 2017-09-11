package com.caojian.myworkapp.update;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.base.BaseActivity;
import com.caojian.myworkapp.until.ActivityControl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by CJ on 2017/8/16.
 */

public class UpdateActivity extends BaseActivity {

    public static void go2UpdateActivity(Context from, UpdateMsg.DataBean data,int requestCode)
    {
        Intent intent = new Intent(from,UpdateActivity.class);
        intent.putExtra("databean",data);
        ((AppCompatActivity)from).startActivityForResult(intent,requestCode);
    }
    @BindView(R.id.tv_update_comments)
    TextView mTv_comments;
    private UpdateMsg.DataBean dataBean;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        unbinder = ButterKnife.bind(this);
        initVariable();
        if(dataBean.getComment() != null)
        {
            mTv_comments.setText(dataBean.getComment());
        }else
        {
            mTv_comments.setText("新版上线");
        }

    }

    private void initVariable() {
        dataBean = (UpdateMsg.DataBean) getIntent().getSerializableExtra("databean");
    }

    /**
     * 暂不更新
     * @param view
     */
    public void updateForLater(View view)
    {
        if(dataBean == null)
        {
            // TODO: 2017/8/18
            return;
        }else {
            if(dataBean.getMandatory().equals("1"))
            {
                ActivityControl.finishActivity();
                return;
            }

            if (dataBean.getMandatory().equals("0"))
            {
                finish();
            }
        }
    }

    public void updateForNow(View v)
    {
        // TODO: 2017/8/18启动服务 下载应用
    }



    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
