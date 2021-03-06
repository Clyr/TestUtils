package com.clyr.testutils.activity;

import android.annotation.SuppressLint;
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
@SuppressLint("NonConstantResourceId")
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
            qqIntent.putExtra(Intent.EXTRA_TEXT, "????????????????????????");
            startActivity(qqIntent);
        } catch (Exception e) {

            if (e instanceof ActivityNotFoundException)
                ToastUtils.showShort("????????????QQ???????????????");
            else
                ToastUtils.showShort("????????????");
        }
    }

    private void shareWeChat() {
        try {
            Intent wechatIntent = new Intent(Intent.ACTION_SEND);
            wechatIntent.setPackage("com.tencent.mm");
            wechatIntent.setType("text/plain");
            wechatIntent.putExtra(Intent.EXTRA_TEXT, "????????????????????????");
            startActivity(wechatIntent);
        } catch (Exception e) {
            if (e instanceof ActivityNotFoundException)
                ToastUtils.showShort("?????????????????????????????????");
            else
                ToastUtils.showShort("????????????");
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
        startActivity(Intent.createChooser(mulIntent, "???????????????"));
    }

    private void shareImg() {
        String path = getResourcesUri(R.drawable.clyr);
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(imageIntent, "??????"));
    }

    private void shareTest() {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.project_github));
        startActivity(Intent.createChooser(textIntent, "??????"));
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        //Toast.makeText(this, "Uri:" + uriPath, Toast.LENGTH_SHORT).show();
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
    }
}
