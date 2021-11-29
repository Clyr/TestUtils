package com.clyr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by 11635 of clyr on 2021/11/26.
 */

public class TitleCustomView extends LinearLayout {

    private LinearLayout title_body, left_lin, right_lin;
    private ImageView title_left_icon, title_right_icon;
    private TextView title_left_text, title_center_text, title_right_text;

    private final int MODE_DEFAULT = -1;
    private final int MODE_ALL_SHOW = 0;
    private final int MODE_ONLY_LEFT = 1;
    private final int MODE_ONLY_RIGHT = 2;
    private final int MODE_ONLY_CENTER = 3;
    private int mode;
    private int leftIcon, rightIcon;
    private String leftText, rightText, centerText;
    private int leftTextColor, centerTextColor, rightTextColor;

    private final int CENTERSIZEDEFAULT = 18;
    private final int SIZEDEFAULT = 16;
    private int leftSize, centerSize, rightSize;
    private int backgroundRes;

    public TitleCustomView(Context context) {
        super(context);
    }

    public TitleCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleCustomView, defStyleAttr, 0);

        mode = array.getInt(R.styleable.TitleCustomView_mode, MODE_DEFAULT);
        leftIcon = array.getResourceId(R.styleable.TitleCustomView_left_icon, R.drawable.back_icon);
        rightIcon = array.getResourceId(R.styleable.TitleCustomView_right_icon, R.drawable.back_icon);

        leftText = array.getString(R.styleable.TitleCustomView_left_text);
        rightText = array.getString(R.styleable.TitleCustomView_right_text);
        centerText = array.getString(R.styleable.TitleCustomView_center_text);

        int blackColor = getResources().getColor(R.color.black);
        leftTextColor = array.getColor(R.styleable.TitleCustomView_left_text_color, blackColor);
        centerTextColor = array.getColor(R.styleable.TitleCustomView_center_text_color, blackColor);
        rightTextColor = array.getColor(R.styleable.TitleCustomView_right_text_color, blackColor);

        leftSize = array.getDimensionPixelSize(R.styleable.TitleCustomView_left_text_size, SIZEDEFAULT);
        centerSize = array.getDimensionPixelSize(R.styleable.TitleCustomView_center_text_size, CENTERSIZEDEFAULT);
        rightSize = array.getDimensionPixelSize(R.styleable.TitleCustomView_right_text_size, SIZEDEFAULT);

        backgroundRes = array.getResourceId(R.styleable.TitleCustomView_bg, getResources().getColor(R.color.white));
        LayoutInflater.from(context).inflate(R.layout.custom_title, this);
        initView();
    }

    private void initView() {
        title_body = findViewById(R.id.title_body);
        left_lin = findViewById(R.id.left_lin);
        right_lin = findViewById(R.id.right_lin);
        title_left_icon = findViewById(R.id.title_left_icon);
        title_right_icon = findViewById(R.id.title_right_icon);
        title_left_text = findViewById(R.id.title_left_text);
        title_center_text = findViewById(R.id.title_center_text);
        title_right_text = findViewById(R.id.title_right_text);

        title_left_icon.setImageResource(leftIcon);
        title_right_icon.setImageResource(rightIcon);
        if (!TextUtils.isEmpty(leftText)) {
            title_left_text.setText(leftText);
        }
        if (!TextUtils.isEmpty(rightText)) {
            title_right_text.setText(rightText);
        }
        if (!TextUtils.isEmpty(centerText)) {
            title_center_text.setText(centerText);
        }
        title_left_text.setTextColor(leftTextColor);
        title_right_text.setTextColor(rightTextColor);
        title_center_text.setTextColor(centerTextColor);

        title_body.setBackgroundResource(backgroundRes);
        init();
    }

    private void init() {
        init(MODE_DEFAULT);
    }

    private void init(int defa) {
        switch (defa) {
            case MODE_DEFAULT:
                modeDefault();
                break;
            case MODE_ALL_SHOW:
                modeAllShow();
                break;
            case MODE_ONLY_LEFT:
                modeDefault();
                break;
            case MODE_ONLY_RIGHT:
                modeOnlyRight();
                break;
            case MODE_ONLY_CENTER:
                modeOnlyCenter();
                break;
        }
    }

    private void modeOnlyCenter() {
        title_left_icon.setVisibility(GONE);
        title_left_text.setVisibility(GONE);
        title_right_icon.setVisibility(GONE);
        title_right_text.setVisibility(GONE);
    }

    private void modeOnlyRight() {
        title_left_icon.setVisibility(GONE);
        title_left_text.setVisibility(GONE);
        title_right_icon.setVisibility(VISIBLE);
        title_right_text.setVisibility(VISIBLE);
    }

    private void modeAllShow() {
        title_left_icon.setVisibility(VISIBLE);
        title_left_text.setVisibility(VISIBLE);
        title_right_icon.setVisibility(VISIBLE);
        title_right_text.setVisibility(VISIBLE);
    }

    private void modeDefault() {
        title_left_icon.setVisibility(VISIBLE);
        title_left_text.setVisibility(VISIBLE);
        title_right_icon.setVisibility(GONE);
        title_right_text.setVisibility(GONE);
    }

    public LinearLayout getTitleBody() {
        return title_body;
    }

    public ImageView getTitleLeftIcon() {
        return title_left_icon;
    }

    public ImageView getTitleRightIcon() {
        return title_right_icon;
    }

    public TextView getTitleLeftText() {
        return title_left_text;
    }

    public TextView getTitleCenterText() {
        return title_center_text;
    }

    public TextView getTitleRightText() {
        return title_right_text;
    }

    public LinearLayout getLeftGroup() {
        return left_lin;
    }

    public LinearLayout getRightGroup() {
        return right_lin;
    }

    public void setLeftOnClickListener(OnClickListener onClickListener) {
        left_lin.setOnClickListener(onClickListener);
    }

    public void setRightOnClickListener(OnClickListener onClickListener) {
        right_lin.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
