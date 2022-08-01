package com.clyr.testutils.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.testutils.base.Const;
import com.clyr.utils.MyLog;
import com.clyr.utils.PublicTools;
import com.clyr.utils.ToastUtils;
import com.clyr.view.CustomFoldView;
import com.clyr.view.MyTextView;
import com.clyr.view.SlideUnlockView;
import com.clyr.view.UnReadView;
import com.clyr.view.captcha.SwipeCaptchaView;
import com.sunfusheng.marqueeview.MarqueeView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomUIActivity extends BaseActivity {


    private AppCompatTextView htmltext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_uiactivity);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        initBar();
        UnReadView unReadView = findViewById(R.id.unreadview);
        EditText editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                unReadView.setText(s.toString());
            }
        });


        SlideUnlockView slideUnlockView = (SlideUnlockView) findViewById(R.id.slideUnlockView);
//    long[] patter = {1000, 1000, 2000, 50};
        //    vibrator.vibrate(patter,0);
        //    vibrator.cancel();
        //    vibrator.vibrate(10);
        //    long[] patter = {1000, 1000, 2000, 50};
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // 设置滑动解锁-解锁的监听
        slideUnlockView.setOnUnLockListener(unLock -> {
            // 如果是true，证明解锁
            if (unLock) {
                // 启动震动器 100ms
                vibrator.vibrate(100);
                // 当解锁的时候，执行逻辑操作，在这里仅仅是将图片进行展示
                // 重置一下滑动解锁的控件
                slideUnlockView.reset();
                // 让滑动解锁控件消失
//                slideUnlockView.setVisibility(View.GONE);
                ToastUtils.showLong("Open");
            }
        });

        findViewById(R.id.captchadialog).setOnClickListener(v -> showCaptchaDialog());

        initMarqueeView();
        findViewById(R.id.alertdialog).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, DialogActivity.class);
            intent.putExtra(Const.TITLE, getResources().getString(R.string.alertdialog));
            startActivity(intent);
        });
        findViewById(R.id.loaddialog).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, LoadActivity.class);
            intent.putExtra(Const.TITLE, getResources().getString(R.string.loaddialog));
            startActivity(intent);
        });

        /*TextView switch_content = findViewById(R.id.switch_content);
        TextView single_content = findViewById(R.id.single_content);
        TextView switch_tag = findViewById(R.id.switch_tag);
        ImageView switch_img = findViewById(R.id.switch_img);

        //String string = getResources().getString(R.string.string_html1);
        String clickString = "《权利通知指引》";
        String string = getResources().getString(R.string.string_html3);
        Spanned spanned = Html.fromHtml(string);
        switch_content.setMovementMethod(LinkMovementMethod.getInstance());

        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(clickString)) {
            int indexStart = string.indexOf(clickString);
            int indexEnd = indexStart + clickString.length();

            SpannableString spannableString = new SpannableString(spanned);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CustomUIActivity.this, "点击了world", Toast.LENGTH_LONG).show();
                    ((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
                    MyLog.loge("点击了MyClickableSpan");
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            MyClickableSpan myClickableSpan = new MyClickableSpan(false, v -> {
                Toast.makeText(CustomUIActivity.this, "点击了MyClickableSpan", Toast.LENGTH_LONG).show();
                //MyLog.loge("点击了MyClickableSpan");
            });

            //spannableString.setSpan(clickableSpan, 0, 5, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            spannableString.setSpan(clickableSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(styleSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            spannableString.setSpan(foregroundColorSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            BackgroundColorSpan span = new BackgroundColorSpan(Color.TRANSPARENT);
            spannableString.setSpan(span, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);


            switch_content.setText(spannableString);

            switch_content.setOnTouchListener((v, event) -> {
                return event.getAction() == MotionEvent.ACTION_MOVE;
                //return true;
            });

            RelativeLayout relative_switch = findViewById(R.id.relative_switch);
            switch_content.setOnClickListener(v -> {
                Toast.makeText(CustomUIActivity.this, "switch_content.setOnClickListener", Toast.LENGTH_LONG).show();
                MyLog.loge("switch_content.setOnClickListener");
            });

            LinearLayout switch_bottom = findViewById(R.id.switch_bottom);
            if (switch_content.getLineCount() > 2) {
                switch_bottom.setVisibility(View.VISIBLE);

            }
        }*/


        htmltext = findViewById(R.id.htmltext);
        //htmltext.setVisibility(View.VISIBLE);
        //htmltext.setText(Html.fromHtml(getResources().getString(R.string.string_android2)));
        //htmltext.setText(getResources().getString(R.string.string_html3));
        htmltext.setText("安卓（Android）是一种基于Linux内核");
        //handler.postDelayed(runnable, 1000);
        htmltext.setOnClickListener(v -> {
            Layout layout = htmltext.getLayout();
            MyLog.loge("htmltext - height = " + htmltext.getHeight() + " textSize = " + htmltext.getTextSize());
            MyLog.loge("layout = " + layout.getHeight() + " lineHeight = " + htmltext.getLineHeight());
            //handler.postDelayed(runnable, 300);
            htmltext.setText(htmltext.getText() + "\n安卓（Android）是一种基于Linux内核");


        });

        TextView textview = findViewById(R.id.textview);
        textview.setText(getResources().getString(R.string.string_html3));

        MyTextView mytextview = findViewById(R.id.mytextview);
        mytextview.setText(getResources().getString(R.string.string_html3));
        //initSwitchView();

        initFoldView();
    }

    private void initFoldView() {
        CustomFoldView foldview = findViewById(R.id.foldview);
        foldview.setHideLines(2);
        foldview.init(getResources().getString(R.string.string_html3), 20 * 2);
        foldview.setClickStringColor(R.color.colorPrimary);
        foldview.addClickString("《权利通知指引》", "免责声明");
        foldview.setOnClickAction((clickString) -> {
            MyLog.loge(clickString);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://baike.baidu.com/item/Android/60243?fr=aladdin")));
        });
        foldview.start();
    }

    StringBuilder stringBuilder = new StringBuilder();
    List<Integer> mlist = new ArrayList<>();
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MyLog.loge("htmltext - height = " + htmltext.getHeight() + " textSize = " + htmltext.getTextSize());
            mlist.add(htmltext.getHeight());
            stringBuilder.append("安卓（Android）是一种基于Linux内核\n");
            htmltext.setText(stringBuilder);
            if (mlist.size() < 20) {
                //handler.postDelayed(runnable, 300);
            } else {
                StringBuilder ss = new StringBuilder();
                StringBuilder ss2 = new StringBuilder();
                StringBuilder ss3 = new StringBuilder();

                for (int i = 0; i < mlist.size(); i++) {

                    if (i <= mlist.size() - 2) {
                        int i1 = mlist.get(i + 1) - mlist.get(i);
                        ss2.append(i1).append(" , ");
                    }
                    ss.append(mlist.get(i)).append(" , ");
                    ss3.append(mlist.get(i) - htmltext.getTextSize() * (i + 1)).append(" , ");
                }
                MyLog.loge(String.valueOf(ss));
                MyLog.loge(String.valueOf(ss2));
                MyLog.loge(String.valueOf(ss3));
            }

        }
    };

    private boolean isShow = false;
    private MyTextView switchContent;
    //    private TextView switchContent;
    private LinearLayout switchLin;
    private final int minLines = 2;
    private final int maxLines = 20;
    private final int margin = 10;
    private String clickString;
    private int indexStart = -1;

    @SuppressLint("ClickableViewAccessibility")
    private void initSwitchView() {
        RelativeLayout switchRelative = findViewById(R.id.switch_relative);
        switchContent = findViewById(R.id.switch_content_text);
        switchLin = findViewById(R.id.switch_lin);
        TextView switchText = findViewById(R.id.switch_text);
        ImageView switchImage = findViewById(R.id.switch_image);


        clickString = "《权利通知指引》";
        String string = getResources().getString(R.string.string_html3);
        switchContent.setText(string);
        switchContent.setClickString(clickString);
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(clickString)) {
            indexStart = string.indexOf(clickString);
            switchLin.setOnClickListener(v -> {

                if (switchContent.getLineCount() >= minLines) {
                    if (isShow) {
                        switchContent.setMaxLines(minLines);
                        try {
                            changeTextViewContent(string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        switchContent.setPadding(0, 0, 0, 0);
                    } else {
                        switchContent.setMinLines(minLines);
                        switchContent.setMaxLines(maxLines);
                        switchContent.setText(string);
                        //reSetPadding(string);
                    }
                }
                if (isShow) {
                    switchText.setText("展开");
                } else {
                    switchText.setText("收起");
                }
                isShow = !isShow;
            });
            switchRelative.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (switchContent.getLineCount() > minLines) {
                        switchLin.setVisibility(View.VISIBLE);
                        switchContent.setMaxLines(maxLines);
                        try {
                            changeTextViewContent(string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    switchRelative.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
           /* switchContent.setCallBack(() -> {
                if (switchContent.getLineCount() > minLines) {
                    switchLin.setVisibility(View.VISIBLE);
                    switchContent.setMaxLines(maxLines);
                    try {
                        changeTextViewContent(string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/
        }
    }


    @SuppressLint("SetTextI18n")
    private void changeTextViewContent(String text) throws Exception {

        if (switchContent.getLineCount() > minLines) {
            Layout layout = switchContent.getLayout();
            int tagWidth = switchLin.getWidth() + margin;
            int textWidth = switchContent.getWidth();
            //MyLog.loge("textWidth = " + textWidth);
            //MyLog.loge("tagWidth = " + tagWidth);
            int maxLineWidth = 0;

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < switchContent.getLineCount(); i++) {
                int start = layout.getLineStart(i);
                int end = layout.getLineEnd(i);
                //指定行的内容

                String line = text.substring(start, end);
                //指定行的宽度
                float widthCount = layout.getLineWidth(i);
                if (widthCount > maxLineWidth) {
                    maxLineWidth = (int) widthCount;
                }

                if (i == minLines - 1) {
                    String stringForMore = getStringForMore(line, textWidth - tagWidth);
                    switchContent.setMaxLines(2);
                    switchContent.setText(stringBuilder + stringForMore);
                    switchLin.setVisibility(View.VISIBLE);
                    //TODO 需要重新添加点击事件
                    //addClickableSpan(switchContent.getText().toString());
                }

                stringBuilder.append(line);

                /*if (maxLineWidth > 0 && (i == switchContent.getLineCount() - 1)) {
                    int rm = textWidth - maxLineWidth;
                    if (rm > 16) {
                        rm = 6;
                    }
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) switchLin.getLayoutParams();
                    layoutParams.rightMargin = PublicTools.dip2px(this, rm);
                    switchLin.setLayoutParams(layoutParams);
                }*/

            }
        }
    }

    private void reSetPadding(@NotNull String string) {
        Layout layout = switchContent.getLayout();
        int tagWidth = switchLin.getWidth() + margin;
        int textWidth = switchContent.getWidth();
        if (switchContent.getLineCount() > minLines) {
            float widthCount = layout.getLineWidth(switchContent.getLineCount() - 1);
            if (widthCount > textWidth - tagWidth) {
                //switchContent.setPadding(0, 0, 0, PublicTools.dip2px(this, 18));
                switchContent.setPadding(0, 0, 0, 0);
            }
        }
        //TODO 需要重新添加点击事件
        addClickableSpan(string);
    }

    private String getStringForMore(String str, int screenWidth) {
        StringBuilder strEnd = new StringBuilder();
        strEnd.append(str);
        Paint paint = new Paint();
        paint.setTextSize(PublicTools.px2dip(this, switchContent.getTextSize()));
        float textMaxWidth = screenWidth - PublicTools.dip2px(this, paint.measureText("..."));
        //MyLog.loge("screenWidth = " + screenWidth + " - textMaxWidth = " + textMaxWidth);
        if (textMaxWidth > 0) {
            while (true) {
                if (strEnd.length() > 1) {
                    float edWidth = paint.measureText(String.valueOf(strEnd));
                    //MyLog.loge("edWidth = "+PublicTools.dip2px(this, edWidth) +"");
                    if (PublicTools.dip2px(this, edWidth) > textMaxWidth) {
                        float measureText = paint.measureText(String.valueOf(strEnd.delete(strEnd.length() - 1, strEnd.length())));
                        //MyLog.loge("measureText = "+PublicTools.dip2px(this, measureText));
                        if (PublicTools.dip2px(this, measureText) <= textMaxWidth) {
                            return String.valueOf(strEnd.append("..."));
                        }
                    } else {
                        return String.valueOf(strEnd.append("..."));
                    }
                } else {
                    return str;
                }
            }
        }
        return str;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addClickableSpan(@NotNull String string) {

        Spanned spanned = Html.fromHtml(string);
        switchContent.setMovementMethod(LinkMovementMethod.getInstance());

        int indexEnd = indexStart + clickString.length();

        if (indexStart > 0 && indexStart < string.length()) {
            if (indexEnd > string.length()) {
                indexEnd = string.length();
            }

            SpannableString spannableString = new SpannableString(spanned);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CustomUIActivity.this, "点击了world", Toast.LENGTH_LONG).show();
                    //((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
                    MyLog.loge("点击了MyClickableSpan");
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            switchContent.setHighlightColor(getResources().getColor(android.R.color.transparent));

            spannableString.setSpan(clickableSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(styleSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
            spannableString.setSpan(foregroundColorSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            switchContent.setText(spannableString);
            switchContent.setOnTouchListener((v, event) -> {
                return event.getAction() == MotionEvent.ACTION_MOVE;
                //return true;
            });

            /*switchContent.setOnClickListener(v -> {
                Toast.makeText(CustomUIActivity.this, "switch_content.setOnClickListener", Toast.LENGTH_LONG).show();
                MyLog.loge("switch_content.setOnClickListener");
            });*/
        }


    }


    /**
     * 格式化超链接文本内容并设置点击处理
     */
    private CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = Html.fromHtml(html);
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }

    /**
     * 设置点击超链接对应的处理内容
     */
    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder, final URLSpan urlSpan) {
        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                MyLog.i("URL-click:" + urlSpan.getURL());
            }
        };
        //实际使用过程中，可根据需要清空之前的span，防止出现重复调用的情况
        //clickableHtmlBuilder.clearSpans();或者clickableHtmlBuilder.removeSpan(urlSpan);
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }


    private void initMarqueeView() {
        String[] res = {
                "静夜思",
                "床前明月光", "疑是地上霜",
                "举头望明月",
                "低头思故乡"
        };
        MarqueeView marqueeView = findViewById(R.id.marqueeView);

        List<String> info = new ArrayList<>();
        info.add("静夜思");
        info.add("床前明月光");
        info.add("疑是地上霜");
        info.add("举头望明月");
        info.add("低头思故乡");
        // marqueeView.startWithList(info);

        // 在代码里设置自己的动画
        marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);

        // String notice = "心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！";
        // marqueeView.startWithText(notice);

        marqueeView.setOnItemClickListener((position, textView) -> ToastUtils.showShort((String) textView.getText()));
    }

    private void showCaptchaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dlg_activity_main, null);
        final SwipeCaptchaView mSwipeCaptchaView = view.findViewById(R.id.swipeCaptchaView);

        final SeekBar mSeekBar = view.findViewById(R.id.dragBar);
        builder.setView(view);
        final AlertDialog alertDialog = builder.show();
        view.findViewById(R.id.btnChange).setOnClickListener(v -> {
            mSwipeCaptchaView.createCaptcha();
            mSeekBar.setEnabled(true);
            mSeekBar.setProgress(0);
        });
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(CustomUIActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
                alertDialog.dismiss();
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                Log.d("CustomUIActivity", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                Toast.makeText(CustomUIActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });

        //测试从网络加载图片是否ok
        Glide.with(this)
                .asBitmap()
                .load(R.drawable.bg_slide_unblock)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }
}