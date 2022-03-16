package com.clyr.testutils.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.WebViewInterface;

public class WebViewActivity extends BaseActivity {

    private WebView mWebview;
    private Button mBtnAction1, mBtnAction2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
    }

    @Override
    protected void initView() {
        initBar();
        initWebView();
        mBtnAction1 = findViewById(R.id.btn_action1);
        mBtnAction1.setOnClickListener(v -> WebViewInterface.getInstance().callJsFunction());
        mBtnAction2 = findViewById(R.id.btn_action2);
        mBtnAction2.setOnClickListener(v -> WebViewInterface.getInstance().callJsFunction("调用JS,有参"));
    }

    private void initWebView() {
        mWebview = findViewById(R.id.webview);
        mWebview.loadUrl("file:///android_asset/wst.html");
        WebViewInterface.getInstance().loadJavaMethod(mWebview);
        WebViewInterface.getInstance().setActivity(this);
    }


}