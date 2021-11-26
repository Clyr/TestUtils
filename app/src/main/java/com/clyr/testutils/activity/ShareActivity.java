package com.clyr.testutils.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;


import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareActivity extends BaseActivity {

    @BindView(R.id.share_text)
    TextView shareText;
    @BindView(R.id.share_img)
    TextView shareImg;
    @BindView(R.id.share_file)
    TextView shareFile;
    @BindView(R.id.share_wechat)
    TextView shareWechat;
    @BindView(R.id.share_qq)
    TextView shareQq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {

    }


    @OnClick({R.id.share_text, R.id.share_img, R.id.share_file, R.id.share_wechat, R.id.share_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_text:
                shareTest();
                break;
            case R.id.share_img:
                shareImg();
                break;
            case R.id.share_file:
                shareFile();
                break;
            case R.id.share_wechat:
                shareWeChat();
                break;
            case R.id.share_qq:
                shareQQ();
                break;
        }
    }

    private void shareQQ() {
        try {
            Intent qqIntent = new Intent(Intent.ACTION_SEND);
            qqIntent.setPackage("com.tencent.mobileqq");
            qqIntent.setType("text/plain");
            qqIntent.putExtra(Intent.EXTRA_TEXT, "分享到微信的内容");
            startActivity(qqIntent);
        } catch (Exception e) {

            if (e instanceof ActivityNotFoundException)
                ToastUtils.showShort("请先安装QQ再进行尝试");
            else
                ToastUtils.showShort("分享失败");
        }
    }

    private void shareWeChat() {
        try {
            Intent wechatIntent = new Intent(Intent.ACTION_SEND);
            wechatIntent.setPackage("com.tencent.mm");
            wechatIntent.setType("text/plain");
            wechatIntent.putExtra(Intent.EXTRA_TEXT, "分享到微信的内容");
            startActivity(wechatIntent);
        } catch (Exception e) {
            if (e instanceof ActivityNotFoundException)
                ToastUtils.showShort("请先安装微信再进行尝试");
            else
                ToastUtils.showShort("分享失败");
        }
    }

    private void shareFile() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        Uri uri1 = Uri.parse(getResourcesUri(R.drawable.clyr));
        Uri uri2 = Uri.parse(getResourcesUri(R.drawable.clyr));
        imageUris.add(uri1);
        imageUris.add(uri2);
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        mulIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(mulIntent, "多文件分享"));
    }

    private void shareImg() {
        String path = getResourcesUri(R.drawable.clyr);
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(imageIntent, "分享"));
    }

    private void shareTest() {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, "https://github.com/Clyr/TestList");
        startActivity(Intent.createChooser(textIntent, "分享"));
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        //Toast.makeText(this, "Uri:" + uriPath, Toast.LENGTH_SHORT).show();
        return uriPath;
    }
}
