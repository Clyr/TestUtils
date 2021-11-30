package com.clyr.testutils.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

public class MarqueeActivity extends BaseActivity {

    String[] res = {
            "静夜思",
            "床前明月光", "疑是地上霜",
            "举头望明月",
            "低头思故乡"
    };
    MarqueeView marqueeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
        init();
    }

    private void init() {
        marqueeView = (MarqueeView) findViewById(R.id.marqueeView);

        List<String> info = new ArrayList<>();
        info.add("静夜思");
        info.add("床前明月光");
        info.add("疑是地上霜");
        info.add("举头望明月");
        info.add("低头思故乡");
//        marqueeView.startWithList(info);

    // 在代码里设置自己的动画
        marqueeView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);

//        String notice = "心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！";
//        marqueeView.startWithText(notice);
//
//// 在代码里设置自己的动画
//        marqueeView.startWithText(notice, R.anim.anim_bottom_in, R.anim.anim_top_out);

        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
//                Toast.makeText(getApplicationContext(), String.valueOf(marqueeView.getPosition()) + ". " + textView.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), String.valueOf(position) + ". " + textView.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        marqueeView.startFlipping();
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onStop() {
        super.onStop();
//        marqueeView.stopFlipping();
    }
}
