package com.clyr.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.clyr.utils.MyLog;
import com.clyr.utils.PublicTools;
import com.clyr.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lzy of clyr
 * @date 2022/06/16
 * 第一版 两端对齐、点击事件、变色
 */

public class CustomFoldView extends View {
    TextPaint mTextPaint;
    Paint mPaint;
    String mText;
    List<LineText> mList = new ArrayList<>();
    private float desiredWidth;
    private int screenWidth;
    private int singleLineNum;
    List<ClickString> clickStringList = new ArrayList();
    private Typeface typeface = Typeface.DEFAULT;
    private float fontSize = 14f;
    private int mColor = R.color.black;
    private OnClickAction onClickAction;

    private int hideLines = -1;
    private float buttonWidth = -1;
    private boolean isHide = false;

    private float padding = 0f;
    private String[] strings;
    private int lineMaxNum;

    public void setOnClickAction(OnClickAction onClickAction) {
        this.onClickAction = onClickAction;
    }

    public CustomFoldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFoldView init(String text, float padding) {
        mPaint = new Paint();
        mTextPaint = new TextPaint();
        defaultPaintText();
        mText = text;
        this.padding = padding;
        measureButton();
        return this;
    }


    public void start() {
        if (!TextUtils.isEmpty(mText)) {

            desiredWidth = StaticLayout.getDesiredWidth(mText, mTextPaint);
            screenWidth = PublicTools.getScreenWidth((Activity) getContext()) - PublicTools.dip2px(getContext(), padding);
            //screenWidth = getMeasuredWidth();

            char[] chars = mText.toCharArray();
            int tempWidth = 0;
            StringBuilder stringBuilder = new StringBuilder();
            mList.clear();
            for (char c : chars) {
                stringBuilder.append(c);
                float desiredWidth = StaticLayout.getDesiredWidth(stringBuilder, mTextPaint);
                if (desiredWidth > screenWidth) {
                    mList.add(new LineText(stringBuilder.substring(0, stringBuilder.length() - 1), desiredWidth));
                    stringBuilder.delete(0, stringBuilder.length());
                    stringBuilder.append(c);
                } else if (mText.lastIndexOf(c) == mText.length() - 1) {
                    mList.add(new LineText(String.valueOf(stringBuilder), desiredWidth));
                }
            }
            lineMaxNum = mList.size();
            if (hideLines > 0) {
                if (StaticLayout.getDesiredWidth(mList.get(mList.size() - 1).getText(), mTextPaint) >
                        screenWidth - PublicTools.dip2px(getContext(), fontSize)) {
                    mList.add(new LineText("", 0));
                }
                lineMaxNum = mList.size();

                if (mList.size() > hideLines && isHide) {
                    mList = mList.subList(0, hideLines);
                }
            }
            if (strings != null && strings.length > 0) {
                for (String item : strings) {
                    recordClickString(item);
                }
            }
            postInvalidate();
            /*setMeasuredDimension(screenWidth,
                    PublicTools.dip2px(getContext(), fontSize * mList.size() + 3));*/
        } else {
            MyLog.loge("The text is empty");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //detailText();

        if (mList.size() > 0) {
            int lineHeight = PublicTools.dip2px(getContext(), fontSize);
            for (int i = 0; i < mList.size(); i++) {
                drawScaledText(canvas, mList.get(i), lineHeight * (i + 1), i == mList.size() - 1);
            }
        }
    }


    private void drawScaledText(Canvas canvas, LineText lineText, float lineY, boolean isLastLine) {
        float x = 0;
        String text = lineText.getText();
        float lineWidth = StaticLayout.getDesiredWidth(text, mTextPaint);
        float rightSpace = (screenWidth - lineWidth - getPaddingLeft() - getPaddingRight()) / (text.length() - 1);

        for (int j = 0; j < text.length(); ++j) {
            String c = String.valueOf(text.charAt(j));
            defaultPaintText();
            float cw = StaticLayout.getDesiredWidth(c, mTextPaint);
            List<ClickString> clickStrings = lineText.getClickStrings();
            for (ClickString item : clickStrings) {
                if (item.getStart() <= j && j < item.getEnd()) {
                    setTextPaintType();
                    break;
                }
            }

            if (hideLines > 0 && isLastLine) {
                int imageWidth = PublicTools.dip2px(getContext(), fontSize);
                float imageSpace = screenWidth - x - StaticLayout.getDesiredWidth("...", mTextPaint) - imageWidth;
                if (imageSpace <= buttonWidth) {
                    canvas.drawText("...", x, lineY, mTextPaint);
                    //x += StaticLayout.getDesiredWidth("...", mTextPaint);
                    float xBtn = screenWidth - buttonWidth;
                    Bitmap mBitmap;
                    if (isHide) {
                        canvas.drawText("展开", xBtn, lineY, mTextPaint);
                        mBitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_down);
                    } else {
                        canvas.drawText("收起", xBtn, lineY, mTextPaint);
                        mBitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.icon_up);
                    }
                    Rect des = new Rect(screenWidth - imageWidth, (int) lineY - imageWidth, screenWidth, (int) lineY);
                    canvas.drawBitmap(mBitmap, null, des, mPaint);

                    return;
                }

            }

            canvas.drawText(c, x, lineY, mTextPaint);
            x += isLastLine ? cw : cw + rightSpace;
        }
    }


    public void addClickString(String... strings) {
        this.strings = strings;

    }

    private void recordClickString(String clickString) {

        int index = 0;
        if (!TextUtils.isEmpty(mText) && !TextUtils.isEmpty(clickString)) {
            while ((index = mText.indexOf(clickString, index)) != -1) {
                ClickString click = new ClickString(index, index + clickString.length(), clickString);
                clickStringList.add(click);
                recordContentList(click);
                index = index + clickString.length();
            }
        }

    }

    private void recordContentList(ClickString click) {
        int length = 0;
        for (int i = 0; i < mList.size(); i++) {
            int textLength = mList.get(i).getText().length();
            length += textLength;

            if (click.getStart() >= length - textLength && click.getEnd() <= length) {
                mList.get(i).addClickString(new ClickString(click.getStart() - (length - textLength),
                        click.getEnd() - (length - textLength), click.getClickString()));
                return;
            } else if (click.getStart() < length && click.getEnd() > length) {
                mList.get(i).addClickString(new ClickString(click.getStart() - (length - textLength),
                        textLength, click.getClickString()));
            } else if (click.getStart() < length - textLength && click.getEnd() <= length) {
                mList.get(i).addClickString(new ClickString(length - textLength,
                        click.getEnd() - (length - textLength), click.getClickString()));
                return;
            }
        }
    }

    private void defaultPaintText() {
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        mTextPaint.setTextSize(PublicTools.dip2px(getContext(), 14f));
    }

    public void setClickStringTypeface(Typeface typeface) {
        this.typeface = typeface;
        setTextPaintType();
    }

    public void setClickStringFontSize(float fontSize) {
        this.fontSize = fontSize;
        setTextPaintType();
    }

    public void setClickStringColor(int mColor) {
        this.mColor = mColor;
        setTextPaintType();
    }

    private void setTextPaintType() {
        mTextPaint.setTypeface(typeface);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), mColor));
        mTextPaint.setTextSize(PublicTools.dip2px(getContext(), fontSize));
    }

    public void setHideLines(int hideLines) {
        this.hideLines = hideLines;
        isHide = hideLines > 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                touchClickString(event);
                break;

            default:
        }
        return true;
    }

    private void touchClickString(MotionEvent event) {
        float upX = event.getX();
        float upY = event.getY();
        int lineHeight = PublicTools.dip2px(getContext(), fontSize);
        //float lineY = upY / lineHeight;
        int lineY = (int) (upY / lineHeight);
        if (lineY < mList.size()) {
            if (lineY == hideLines - 1) {
                if (upX <= screenWidth && upX >= screenWidth - buttonWidth) {
                    isHide = !isHide;
                    start();
                    return;
                }

            }

            List<ClickString> clickStrings = mList.get(lineY).getClickStrings();
            String text = mList.get(lineY).getText();
            float lineWidth = StaticLayout.getDesiredWidth(text, mTextPaint);
            float rightSpace = (screenWidth - lineWidth - getPaddingLeft() - getPaddingRight()) / (text.length() - 1);

            if (clickStrings.size() > 0) {
                for (ClickString cs : clickStrings) {
                    float csStartWidth = StaticLayout.getDesiredWidth(text.substring(0, cs.getStart()), mTextPaint);
                    float csEndWidth = StaticLayout.getDesiredWidth(text.substring(0, cs.getStart() + cs.getClickString().length()), mTextPaint)
                            + (cs.getStart() + cs.getClickString().length()) * rightSpace;
                    if (csStartWidth + cs.getStart() * rightSpace <= upX &&
                            upX <= csEndWidth) {
                        if (onClickAction != null) {
                            onClickAction.onClick(cs.getClickString());
                        }
                        return;
                    }

                }
            }
        }
    }

    private void getTouchChar(MotionEvent event) {
        float upX = event.getX();
        float upY = event.getY();
        int lineHeight = PublicTools.dip2px(getContext(), fontSize);
        //float lineY = upY / lineHeight;
        int lineY = (int) (upY / lineHeight);
        if (lineY < mList.size()) {
            String text = mList.get(lineY).getText();

            float lineWidth = StaticLayout.getDesiredWidth(text, mTextPaint);
            float rightSpace = (screenWidth - lineWidth - getPaddingLeft() - getPaddingRight()) / (text.length() - 1);
            for (int i = 0; i < text.length(); i++) {
                String c = String.valueOf(text.charAt(i));
                float cw = StaticLayout.getDesiredWidth(c, mTextPaint);
                if (lineY == mList.size() - 1) {
                    upX -= cw;
                } else {
                    upX -= cw + rightSpace;
                }
                if (upX <= 0) {
                    ToastUtils.showShort(c);
                    return;
                }
            }
        }
    }

    private void measureButton() {
        buttonWidth = StaticLayout.getDesiredWidth("展开", mTextPaint);
        buttonWidth += PublicTools.dip2px(getContext(), 1 + fontSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        //detailText(width);
        setMeasuredDimension(width,
                PublicTools.dip2px(getContext(), fontSize * lineMaxNum + 3));
    }

    class LineText {
        private String text;
        private float lineWidth;
        private List<ClickString> clickStrings = new ArrayList<>();

        public LineText(String text, float lineWidth) {
            this.text = text;
            this.lineWidth = lineWidth;
        }

        public String getText() {
            return text;
        }

        public float getLineWidth() {
            return lineWidth;
        }

        public void addClickString(ClickString cs) {
            clickStrings.add(cs);
        }

        public List<ClickString> getClickStrings() {
            return clickStrings;
        }

        @NonNull
        @Override
        public String toString() {
            return "LineText{" +
                    "text='" + text + '\'' +
                    ", lineWidth=" + lineWidth +
                    '}';
        }
    }

    class ClickString {
        private int start;
        private int end;
        private String clickString;

        public ClickString(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public ClickString(int start, int end, String clickString) {
            this.start = start;
            this.end = end;
            this.clickString = clickString;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public String getClickString() {
            return clickString;
        }

        @NonNull
        @Override
        public String toString() {
            return "ClickString{" +
                    "start=" + start +
                    ", end=" + end +
                    ", clickString='" + clickString + '\'' +
                    '}';
        }
    }

    public interface OnClickAction {
        void onClick(String clickString);
    }
}
