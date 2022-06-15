package com.clyr.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.clyr.utils.MyLog;
import com.clyr.utils.PublicTools;

/**
 * @author lzy of clyr
 * @date 2022/06/15
 */

public class MyTextView extends AppCompatTextView {

    private String textContent;
    private int mViewWidth;
    private int width;
    private int mViewHeight;
    private String clickString;
    private int indexStart;
    private int indexEnd;
    private int tempEnd = -1;
    private MyTextCallBack callBack;

    private int maxLines = 2;


    public MyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setText(String text) {
        textContent = text;
    }

    public void setMaxLine(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(textContent)) {
            if (getLineCount() > 1) {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
                mViewWidth = getMeasuredWidth();
                mViewHeight = getMeasuredHeight();
                TextPaint paint = getPaint();
                int height = getHeight();
                Layout layout = getLayout();
                tempEnd = -1;
                float lineSpace = (mViewHeight - (getLineCount() * getTextSize()) - PublicTools.dip2px(getContext(), 1)) / (getLineCount() - 1);
                for (int i = 0; i < getLineCount(); i++) {
                    int lineStart = layout.getLineStart(i);
                    int lineEnd = layout.getLineEnd(i);
                    tempEnd += lineEnd;
                    String lineText = textContent.substring(lineStart, lineEnd);
                    if (i == getLineCount() - 1) {
                        canvas.drawText(lineText, 0, getTextSize() * (i + 1) + i * lineSpace, paint);
                    } else {
                        boolean isBold = false;
                        if (!TextUtils.isEmpty(clickString) && lineText.contains(clickString)) {
                            isBold = true;
                        } else {
                            if (indexStart >= (tempEnd - lineEnd) && indexStart <= tempEnd) {
                                isBold = true;
                            } else if (indexEnd >= (tempEnd - lineEnd) && indexEnd < tempEnd) {
                                isBold = true;
                            }
                        }
                        drawScaledText(canvas, paint, lineText, getTextSize() * (i + 1) + i * lineSpace, isBold);

                    }

                }
                if (callBack != null) {
                    callBack.initView();
                }
            } else {
                super.onDraw(canvas);
            }
        } else {
            super.onDraw(canvas);
        }

    }

    private void drawScaledText(Canvas canvas, TextPaint paint, String lineText, float lineY, boolean isBold) {
        float x = 0;
        float lineWidth = StaticLayout.getDesiredWidth(lineText, paint);
        float rightSpace = (mViewWidth - lineWidth - getPaddingLeft() - getPaddingRight()) / (lineText.length() - 1);
        int boldStart = -1;
        int boldEnd = -1;
        if (isBold) {
            if (!TextUtils.isEmpty(clickString) && !TextUtils.isEmpty(lineText)) {
                if (lineText.contains(clickString)) {
                    boldStart = lineText.indexOf(clickString);
                    boldEnd = boldStart + clickString.length();
                } else {
                    boldStart = indexStart - (tempEnd - clickString.length());
                    boldEnd = indexEnd - (tempEnd - clickString.length());
                }
            }
        }
        for (int j = 0; j < lineText.length(); ++j) {
            String c = String.valueOf(lineText.charAt(j));
            paint.setTypeface(Typeface.DEFAULT);
            if (isBold) {
                if (!TextUtils.isEmpty(clickString) && clickString.contains(c)) {
                    if (boldStart <= j && j <= boldEnd) {
                        paint.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }
            }
            float cw = StaticLayout.getDesiredWidth(c, paint);
            canvas.drawText(c, x, lineY, paint);
            x += cw + rightSpace;
        }
    }

    public void setClickString(String clickString) {
        this.clickString = clickString;
        this.clickString = "《权利通知指引》";
        if (!TextUtils.isEmpty(textContent) && !TextUtils.isEmpty(clickString)) {
            indexStart = textContent.indexOf(clickString);
            indexEnd = indexStart + clickString.length();
        }

    }

    /**
     * 获取TextView某一个字符的坐标位置
     *
     * @return 返回的是相对坐标
     * @parms tv
     * @parms index 字符索引
     */
    public static Rect getTextViewSelectionRect(TextView tv, int index) {
        Layout layout = tv.getLayout();
        Rect bound = new Rect();
        int line = layout.getLineForOffset(index);
        layout.getLineBounds(line, bound);
        int yAxisBottom = bound.bottom;//字符底部y坐标
        int yAxisTop = bound.top;//字符顶部y坐标
        int xAxisLeft = (int) layout.getPrimaryHorizontal(index);//字符左边x坐标
        int xAxisRight = (int) layout.getSecondaryHorizontal(index);//字符右边x坐标
        //xAxisRight 位置获取后发现与字符左边x坐标相等，如知道原因请告之。暂时加上字符宽度应对。
        if (xAxisLeft == xAxisRight) {
            String s = tv.getText().toString().substring(index, index + 1);//当前字符
            xAxisRight = xAxisRight + (int) tv.getPaint().measureText(s);//加上字符宽度
        }
        int tvTop = tv.getScrollY();//tv绝对位置
        return new Rect(xAxisLeft, yAxisTop + tvTop, xAxisRight, yAxisBottom + tvTop);

    }

    /**
     * 获取TextView触点坐标下的字符
     *
     * @param tv tv
     * @param x  触点x坐标
     * @param y  触点y坐标
     * @return 当前字符
     */
    public static String getTextViewSelectionByTouch(TextView tv, float x, float y) {
        String s = "";
        for (int i = 0; i < tv.getText().length(); i++) {
            Rect rect = getTextViewSelectionRect(tv, i);
            if (x < rect.right && x > rect.left && y < rect.bottom && y > rect.top) {
                s = tv.getText().toString().substring(i, i + 1);//当前字符
                break;
            }
        }
        return s;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                String byTouch = getTextViewSelectionByTouch(this, event.getX(), event.getY());
                MyLog.loge(byTouch);
                break;

            default:
        }
        return super.onTouchEvent(event);
    }

    public void setCallBack(MyTextCallBack callBack) {
        this.callBack = callBack;
    }

    public interface MyTextCallBack {
        void initView();
    }
/*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);

        int height = (int) (getLineCount() * getTextSize());
        setMeasuredDimension(width, height);
    }*/

}
