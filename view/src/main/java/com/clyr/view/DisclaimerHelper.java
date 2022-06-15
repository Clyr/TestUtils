package com.clyr.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;


import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author lzy of clyr
 * @date 2022/06/14
 */

public class DisclaimerHelper {
    private boolean isShow = false;
    private TextView switchContent;
    private LinearLayout switchLin;
    private final int minLines = 2;
    private final int maxLines = 20;
    private final int margin = 10;
    private String clickString;
    private int indexStart = -1;
    private Context mCon;
    private String strUrl;


   /* @SuppressLint("ClickableViewAccessibility")
    public void initSwitchView(Activity view, Object result) {
        try {
            this.mCon = view;
            RelativeLayout switchRelative = view.findViewById(R.id.switch_relative);
            switchContent = view.findViewById(R.id.switch_content_text);
            switchLin = view.findViewById(R.id.switch_lin);
            TextView switchText = view.findViewById(R.id.switch_text);
            ImageView switchImage = view.findViewById(R.id.switch_image);


            if (result == null) {
                return;
            }
            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            Map<String, String> map = GsonUtil.fromJson(result, type);

            String string = map.get("txt");
            clickString = map.get("link");
            strUrl = map.get("data");

            switchContent.setText(string);
            if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(clickString) && !TextUtils.isEmpty(strUrl)) {
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
                            reSetPadding(string);
                        }
                    }
                    if (isShow) {
                        switchText.setText("展开");
                        switchImage.setImageDrawable(mCon.getResources().getDrawable(R.mipmap.icon_down));
                    } else {
                        switchImage.setImageDrawable(mCon.getResources().getDrawable(R.mipmap.icon_up));
                        switchText.setText("收起");
                        if (view instanceof PeopleResumeActivity) {
                            ScrollView scroll_layout = view.findViewById(R.id.scroll_layout);
                            scroll_layout.post(() -> {
                                scroll_layout.fullScroll(View.FOCUS_DOWN);
                            });
                        } else if (view instanceof RecruitDetailActivity) {
                            NestedScrollView scroll_layout = view.findViewById(R.id.sv_content);
                            scroll_layout.post(() -> {
                                scroll_layout.fullScroll(View.FOCUS_DOWN);
                            });
                        }

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
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                    switchContent.setText(stringBuilder + stringForMore);
                    switchLin.setVisibility(View.VISIBLE);
                    //TODO 需要重新添加点击事件
                    if (!TextUtils.isEmpty(strUrl)) {
                        addClickableSpan(switchContent.getText().toString());
                    }

                }

                stringBuilder.append(line);

               *//* if (maxLineWidth > 0 && (i == switchContent.getLineCount() - 1)) {
                    int rm = textWidth - maxLineWidth;
                    if (rm > 16) {
                        rm = 6;
                    }
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) switchLin.getLayoutParams();
                    layoutParams.rightMargin = PublicTools.dip2px(mCon, rm);
                    switchLin.setLayoutParams(layoutParams);
                }*//*

            }
        }
    }

    private void reSetPadding(@NonNull String string) {
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
        if (!TextUtils.isEmpty(strUrl)) {
            addClickableSpan(string);
        }

    }

    private String getStringForMore(String str, int screenWidth) {
        StringBuilder strEnd = new StringBuilder();
        strEnd.append(str);
        Paint paint = new Paint();
        paint.setTextSize(PublicTools.px2dip(mCon, switchContent.getTextSize()));
        float textMaxWidth = screenWidth - PublicTools.dip2px(mCon, paint.measureText("..."));
        //MyLog.loge("screenWidth = " + screenWidth + " - textMaxWidth = " + textMaxWidth);
        if (textMaxWidth > 0) {
            while (true) {
                if (strEnd.length() > 1) {
                    float edWidth = paint.measureText(String.valueOf(strEnd));
                    //MyLog.loge("edWidth = "+PublicTools.dip2px(this, edWidth) +"");
                    if (PublicTools.dip2px(mCon, edWidth) > textMaxWidth) {
                        float measureText = paint.measureText(String.valueOf(strEnd.delete(strEnd.length() - 1, strEnd.length())));
                        if (PublicTools.dip2px(mCon, measureText) <= textMaxWidth) {
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
    private void addClickableSpan(@NonNull String string) {

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
                    //((TextView) view).setHighlightColor(getResources().getColor(android.R.color.transparent));
                    mCon.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(strUrl)));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                }
            };
            switchContent.setHighlightColor(mCon.getResources().getColor(android.R.color.transparent));
            spannableString.setSpan(clickableSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            *//*StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
            spannableString.setSpan(styleSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);*//*

            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(mCon, R.color.main_background_android));
            spannableString.setSpan(foregroundColorSpan, indexStart, indexEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            switchContent.setText(spannableString);
            switchContent.setOnTouchListener((v, event) -> {
                return event.getAction() == MotionEvent.ACTION_MOVE;
                //return true;
            });

            *//*switchContent.setOnClickListener(v -> {
                Toast.makeText(CustomUIActivity.this, "switch_content.setOnClickListener", Toast.LENGTH_LONG).show();
                MyLog.loge("switch_content.setOnClickListener");
            });*//*
        }


    }*/
}
