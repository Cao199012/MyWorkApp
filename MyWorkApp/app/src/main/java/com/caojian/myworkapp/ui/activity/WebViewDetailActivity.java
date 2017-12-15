package com.caojian.myworkapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.caojian.myworkapp.R;
import com.caojian.myworkapp.model.response.UpdateResponse;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.until.Until;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewDetailActivity extends BaseTitleActivity {
    public static void go2WebViewDetailActivity(Context from, int msgKind)
    {
        Intent intent = new Intent(from,WebViewDetailActivity.class);
        intent.putExtra("kind",msgKind);
        from.startActivity(intent);
    }
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.web_container)
    FrameLayout mContainer;
    WebView mWebView;
    String[] msgUrl = {"getRegisterProtocol.do","getPrivacyProtocol.do"};
    String[] titles = {"服务条款","隐私条款"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle(titles[getIntent().getIntExtra("kind",1)]);

        mWebView = new WebView(WebViewDetailActivity.this);
        WebSettings  set =   mWebView.getSettings();
       set.setJavaScriptEnabled(true);
        mContainer.addView(mWebView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.loadUrl(Until.HTTP_BASE_URL+msgUrl[getIntent().getIntExtra("kind",1)]);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }
}
