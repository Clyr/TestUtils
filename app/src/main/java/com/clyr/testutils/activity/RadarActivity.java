package com.clyr.testutils.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.view.RadarImageView;

public class RadarActivity extends BaseActivity {
    LinearLayout mLin;
    private String[] titles = {"预警", "网络中断", "报警", "数据获取异常"};
    private float[] values = {8, 6, 3, 10}; // 各维度分值
    //    private LifeWheelRadarGraph mLf;
    private RadarImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);

    }

    @Override
    protected void initView() {
        mLin = findViewById(R.id.lin);
        /*mLf = LifeWheelRadarGraph.newInstance(this, titles,
                values,15);*/
        /*addContentView(lf, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/
        mImageView = RadarImageView.newInstance(this, titles, values, 16);
        mLin.addView(mImageView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageView.changeValues(new float[]{(float) (Math.random() * 10), (float) (Math.random() * 10), (float) (Math.random() * 10), (float) (Math.random() * 10)});
            }
        });
    }
}
