package com.clyr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.clyr.utils.ScreenUtil;

/**
 * Created by 11635 of clyr on 2021/12/13.
 */

public class UnReadView extends AppCompatTextView {

    private final int mNormalSize = ScreenUtil.getPxByDp(16f);
    private Paint mPaint;

    public UnReadView(Context context) {
        super(context);
        init();
    }

    public UnReadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnReadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.read_dot_bg));
        setTextColor(Color.WHITE);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int pxByDp = ScreenUtil.getPxByDp(3);
        if (getText().length() == 0) {
            // 没有字符，就在本View中心画一个小圆点
            int l = (getMeasuredWidth() - ScreenUtil.getPxByDp(8)) / 2;
            int r = getMeasuredWidth() - l;
            canvas.drawOval(new RectF(l, l, r, r), mPaint);
        } else if (getText().length() == 1) {
            canvas.drawOval(new RectF(0, 0, mNormalSize, mNormalSize), mPaint);
            //canvas.drawOval(new RectF(pxByDp/2, pxByDp, mNormalSize - pxByDp/2 , mNormalSize), mPaint);
        } else if (getText().length() > 1) {
            canvas.drawRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), getMeasuredHeight() >> 1, getMeasuredHeight() >> 1, mPaint);
        }
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mNormalSize;
        if (getText().length() > 1) {
            width = mNormalSize + ScreenUtil.getPxByDp((getText().length() - 1) * 10);
        }
        setMeasuredDimension(width, mNormalSize);
    }
}
