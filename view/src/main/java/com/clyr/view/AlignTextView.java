package com.clyr.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 *
 * @author lzy of clyr
 * @date 2022/06/10
 */

public class AlignTextView extends AppCompatTextView {

   private boolean alignOnlyOneLine;

   public AlignTextView(Context context) {
      this(context, null);
   }

   public AlignTextView(Context context, @Nullable AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public AlignTextView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      init(context, attrs);
   }

   private void init(Context context, AttributeSet attrs) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlignTextView);
      alignOnlyOneLine = typedArray.getBoolean(R.styleable.AlignTextView_alignOnlyOneLine, false);
      typedArray.recycle();
   }

   @Override
   protected void onDraw(Canvas canvas) {

      TextPaint paint = getPaint();
      paint.setColor(getCurrentTextColor());
      paint.drawableState = getDrawableState();

      CharSequence content = getText();
      if (!(content instanceof String)) {
         super.onDraw(canvas);
         return;
      }
      String text = (String) content;
      Layout layout = getLayout();

      for (int i = 0; i < layout.getLineCount(); ++i) {
         int lineBaseline = layout.getLineBaseline(i) + getPaddingTop();
         int lineStart = layout.getLineStart(i);
         int lineEnd = layout.getLineEnd(i);
         if (alignOnlyOneLine && layout.getLineCount() == 1) {//只有一行
            String line = text.substring(lineStart, lineEnd);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint);
            this.drawScaledText(canvas, line, lineBaseline, width, paint);
         } else if (i == layout.getLineCount() - 1) {//最后一行
            canvas.drawText(text.substring(lineStart), getPaddingLeft(), lineBaseline, paint);
            break;
         } else {//中间行
            String line = text.substring(lineStart, lineEnd);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint);
            this.drawScaledText(canvas, line, lineBaseline, width, paint);
         }
      }

   }

   private void drawScaledText(Canvas canvas, String line, float baseLineY, float lineWidth, TextPaint paint) {

      if (line.length() < 1) {
         return;
      }
      float x = getPaddingLeft();
      boolean forceNextLine = line.charAt(line.length() - 1) == 10;
      int length = line.length() - 1;
      if (forceNextLine || length == 0) {
         canvas.drawText(line, x, baseLineY, paint);
         return;
      }

      float d = (getMeasuredWidth() - lineWidth - getPaddingLeft() - getPaddingRight()) / length;

      for (int i = 0; i < line.length(); ++i) {
         String c = String.valueOf(line.charAt(i));
         float cw = StaticLayout.getDesiredWidth(c, paint);
         canvas.drawText(c, x, baseLineY, paint);
         x += cw + d;
      }
   }

}

