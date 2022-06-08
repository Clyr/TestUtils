package com.clyr.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @author lzy of clyr
 * @date 2022/06/08
 */

public class MyClickableSpan extends ClickableSpan {

    private boolean showLine = true;
    private View.OnClickListener onClickListener;


    public MyClickableSpan( boolean showLine, View.OnClickListener onClickListener) {
        this.showLine = showLine;
        this.onClickListener = onClickListener;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }

    @Override
    public void onClick(@androidx.annotation.NonNull View widget) {
        if (onClickListener != null) {
            onClickListener.onClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(showLine);
    }


}
