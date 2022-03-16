package com.clyr.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by lzy of clyr on 2022/01/20.
 */

public class WebViewInterface {
    private static WebViewInterface mInterFace;

    private WebView mWebview;
    private Activity activity;

    public static WebViewInterface getInstance() {
        if (mInterFace == null) {
            synchronized (WebViewInterface.class) {
                if (mInterFace == null) {
                    mInterFace = new WebViewInterface();
                }
            }
        }
        return mInterFace;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadJavaMethod(WebView webView) {
        mWebview = webView;
        webView.addJavascriptInterface(this, "webviewinterface");
        WebSettings webSettings = mWebview.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        //webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        //webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

        /*//支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        */
        // 5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int position) {
                if (position == 100) {

                }
                super.onProgressChanged(view, position);
            }
        });

    }

    @JavascriptInterface
    public void showToast() {
        ToastUtils.showShort("Toast，没有参数");
    }

    @JavascriptInterface
    public void showToast(String msg) {
        ToastUtils.showShort(msg);
    }

    @JavascriptInterface
    public void jsToJavaTojs() {
        if (mWebview == null || activity == null) {
            ToastUtils.showShort("params:webview|activity is Empty");
            return;
        }
        MyLog.d("jsToJavaTojs()");

        activity.runOnUiThread(() -> mWebview.loadUrl("javascript:javacalljsAlert()"));
    }

    public void callJsFunction() {
        if (mWebview == null) {
            ToastUtils.showShort("params:webview is Empty");
            return;
        }
        mWebview.loadUrl("javascript:javacalljs()");
    }

    public void callJsFunction(String msg) {
        if (mWebview == null) {
            ToastUtils.showShort("params:webview is Empty");
            return;
        }
        mWebview.loadUrl("javascript:javacalljswithargs('" + "Java 调用 Js" + "')");
    }

    @JavascriptInterface
    public void javaAlert() {
        ToastUtils.showShort("params:activity is Empty");
    }
}
