package com.clyr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.view.View;

/**
 * Created by clyr on 2018/3/22 0022.
 * 雷达图
 */

public class RadarImageView extends View {
    private static int count = 4;
    private final float angle = 360 / count;
    private final int point_radius = 1; // 画点的半径
    private final int regionwidth = 40; // 选择分值小区域宽度
    private final int valueRulingCount = 5; // 画等分值线
    private int radius;
    private int centerX;
    private int centerY;
    private static String[] titles = {"工作", "财富", "健康", "娱乐"/*, "家庭", "社交",
            "精神", "贡献"*/};
    private static final RadarImageView lfrg = null;
    private Point[] pts; // 维度端点
    private Region[] regions; // 打分点区域
    private float[] regionValues; // 打分点分数
    private Path valuePath;
    private static float[] values = {8, 6, 8, 6/*, 6, 6, 4, 5*/}; // 各维度分值
    private final int maxValue = 10;
    private Point[] value_pts; // 维度端点
    private Paint paint;
    private Paint valuePaint;
    private static int mTextSize = 15;
    public RadarImageView(Context context) {
        super(context);
        init();
    }
    public static RadarImageView newInstance(Context context,
                                             String[] titile, float[] values, int textSize) {
        titles = titile;
        RadarImageView.values = values;
        count = titile.length;
        mTextSize = textSize;
        return new RadarImageView(context);
    }
    private void init() {
        paint = new Paint();
        valuePaint = new Paint();
        pts = new Point[count];
        value_pts = new Point[count];
        valuePath = new Path();
        for (int i = 0; i < count; i++) {
            pts[i] = new Point();
            value_pts[i] = new Point();
        }
        regionValues = new float[count * valueRulingCount * 2];
        regions = new Region[count * valueRulingCount * 2];
        for (int i = 0; i < regions.length; i++) {
            regions[i] = new Region();
        }
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w) / 2 - 40;
        centerX = w / 2;
        centerY = h / 2;

        for (int i = 0; i < count; i++) {
            pts[i].x = centerX
                    + (int) (radius * Math.cos(Math.toRadians(angle * i)));
            pts[i].y = centerY
                    - (int) (radius * Math.sin(Math.toRadians(angle * i)));

            for (int j = 1; j <= valueRulingCount * 2; j++) {
                int x = centerX + (pts[i].x - centerX) / (valueRulingCount * 2)
                        * j;
                int y = centerY + (pts[i].y - centerY) / (valueRulingCount * 2)
                        * j;
                regions[i * valueRulingCount * 2 + j - 1].set(x - regionwidth
                        / 2, y - regionwidth / 2, x + regionwidth / 2, y
                        + regionwidth / 2);
                regionValues[i * valueRulingCount * 2 + j - 1] = j;
            }
        }
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    public void changeValues(float[] values) {
        this.values = values;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);/* 设置画布的颜色 */
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY); // 画边框线
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i = 0; i < count; i++) {
            int end = i + 1 == count ? 0 : i + 1;

            for (int j = 1; j <= valueRulingCount; j++) {
                canvas.drawLine(centerX + (pts[i].x - centerX) / 5 * j, centerY
                        + (pts[i].y - centerY) / 5 * j, centerX
                        + (pts[end].x - centerX) / 5 * j, centerY
                        + (pts[end].y - centerY) / 5 * j, paint);
            }

            canvas.drawLine(centerX, centerY, pts[i].x, pts[i].y, paint);
        }

        // 写文字
        paint.setTextSize(mTextSize);
        paint.setColor(Color.BLACK);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHegiht = -fontMetrics.ascent;
        for (int i = 0; i < count; i++) {
            if ((angle * i == 90.0) || (angle * i == 270.0))
                paint.setTextAlign(Paint.Align.CENTER);
            else if ((angle * i < 90) || (angle * i > 270))
                paint.setTextAlign(Paint.Align.LEFT);
            else if ((angle * i > 90) || (angle * i < 270))
                paint.setTextAlign(Paint.Align.RIGHT);

            if (angle * i == 270.0)
                canvas.drawText(titles[i], pts[i].x, pts[i].y + fontHegiht,
                        paint);
            else
                canvas.drawText(titles[i], pts[i].x, pts[i].y, paint);
        }

        // 画方向盘分值区域
        for (int i = 0; i < count; i++) {
            value_pts[i].x = (int) (centerX + (pts[i].x - centerX) * values[i]
                    / maxValue);
            value_pts[i].y = (int) (centerY + (pts[i].y - centerY) * values[i]
                    / maxValue);
        }

        valuePath.reset();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        for (int i = 0; i < pts.length; i++) {
            // 给valuePath赋值
            if (i == 0)
                valuePath.moveTo(value_pts[i].x, value_pts[i].y);
            else
                valuePath.lineTo(value_pts[i].x, value_pts[i].y);
            // 画取分圆圈
            canvas.drawCircle(value_pts[i].x, value_pts[i].y, point_radius,
                    paint);
        }
        valuePaint.setAlpha(150);
        canvas.drawPath(valuePath, valuePaint);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
