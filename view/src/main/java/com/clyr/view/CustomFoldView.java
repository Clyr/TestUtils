package com.clyr.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.clyr.utils.MyLog;
import com.clyr.utils.PublicTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzy of clyr
 * @date 2022/06/16
 */

public class CustomFoldView extends View {
    TextPaint mTextPaint;
    Paint mPaint;
    String mText;
    List<String> mList = new ArrayList<>();
    private float desiredWidth;
    private int screenWidth;
    private int lines;
    private int singleLineNum;

    public CustomFoldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(String text) {
        mPaint = new Paint();
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(PublicTools.dip2px(getContext(), 14f));
        mText = text;
        detailText();
    }

    private void detailText() {
        if (!TextUtils.isEmpty(mText)) {

            desiredWidth = StaticLayout.getDesiredWidth(mText, mTextPaint);
            screenWidth = PublicTools.getScreenWidth((Activity) getContext());
            lines = (int) (desiredWidth / screenWidth + 1);

            MyLog.loge("desiredWidth = " + desiredWidth);
            MyLog.loge("lines = " + lines);
            singleLineNum = mText.length() / lines;

            char[] chars = mText.toCharArray();
            int tempWidth = 0;
            StringBuilder stringBuilder = new StringBuilder();
            mList.clear();
            for (char c : chars) {
                stringBuilder.append(c);
                if (StaticLayout.getDesiredWidth(stringBuilder, mTextPaint) > screenWidth) {
                    mList.add(stringBuilder.substring(0, stringBuilder.length() - 1));
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append(c);
                }
            }

            MyLog.loge(mList.toArray().toString());


        } else {
            new NullPointerException("The text isn't Null");
        }
    }
    /*int sStart = 0;
    private void initList(int sEnd){
        //int sEnd = singleLineNum;
        while (mList.size() <= lines) {
            if (StaticLayout.getDesiredWidth(mText.substring(sStart, sEnd), mTextPaint) > screenWidth) {

            } else {
                mList.add(mText.substring(sStart, sEnd));
            }
        }
    }*/


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

    }


    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = 100;
        setMeasuredDimension(width,height);

    }*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*//用于获取设定的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 用于获取设定的长度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 类似这样的判断，后面不过多复述
        // 用于判断是不是wrap_content
        // 如果不进行处理，效果会是match_parent
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(20, 20);
        }*/
    }
}
