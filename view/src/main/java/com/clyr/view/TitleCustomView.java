package com.clyr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by 11635 of clyr on 2021/11/26.
 */

public class TitleCustomView extends LinearLayout {

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    public TitleCustomView(Context context) {
        super(context);
    }

    public TitleCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCornerImageView, defStyleAttr, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.MyCornerImageView_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.MyCornerImageView_boder_color, DEFAULT_BORDER_COLOR);

        LayoutInflater.from(context).inflate(R.layout.custom_title, this);
        initView();
    }

    private void initView() {

    }


}
