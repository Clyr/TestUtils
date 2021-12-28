package com.clyr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by clyr on 2018/1/22 0022.
 */

@SuppressLint("AppCompatCustomView")
public class ProcessImageView extends ImageView {
    private Paint mPaint;// 画笔
    int width = 0;
    int height = 0;
    Context context = null;
    int progress = 0;
    String mPro = "0%";

    public ProcessImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public ProcessImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(Color.parseColor("#70000000"));// 半透明
        canvas.drawRect(0, 0, getWidth(), getHeight() - getHeight() * progress / 100, mPaint);

        mPaint.setColor(Color.parseColor("#00000000"));// 全透明
        canvas.drawRect(0, getHeight() - getHeight() * progress / 100,
                getWidth(), getHeight(), mPaint);

        mPaint.setTextSize(24);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        mPaint.setStrokeWidth(2);
        Rect rect = new Rect();
        mPaint.getTextBounds("100%", "00".length(), "100%".length(), rect);// 确定文字的宽度
//        mPaint.getTextBounds("100%", "0%".length(), "100%".length(), rect);// 确定文字的宽度
        canvas.drawText(mPro, (getWidth() - rect.width()) >> 1,
                (getHeight() + rect.height()) >> 1, mPaint);

    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress == 100) {
            mPro = "";
        } else {
            if (progress >= 0 && progress < 10) {
                mPro = "0" + progress + "%";
            } else {
                mPro = progress + "%";
            }
        }
        postInvalidate();
    }

    ;

}
