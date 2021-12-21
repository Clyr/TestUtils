package com.clyr.three_sdk.baidumapsdk.demo.util;//package com.clyr.three_sdk.baidumapsdk.demo.util;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PointF;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.View;
//
//public final class CustomMapPreviewPointsView extends View {
//
//    PointF[] points;
//    private Paint paint;
//
//    public CustomMapPreviewPointsView(Context context) {
//        super(context);
//        init();
//    }
//
//    public CustomMapPreviewPointsView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    public CustomMapPreviewPointsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//    }
//
//    private void init() {
//        paint = new Paint();
//        paint.setColor(Color.YELLOW);
//        paint.setStyle(Paint.Style.FILL);
//    }
//
//    public void setPoints(PointF[] points) {
//        this.points = points;
//        invalidate();
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//        if (points != null) {
//            for (PointF pointF : points) {
//                canvas.drawCircle(pointF.x, pointF.y, 10, paint);
//            }
//        }
//    }
//}
