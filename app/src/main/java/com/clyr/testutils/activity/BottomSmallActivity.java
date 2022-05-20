package com.clyr.testutils.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.utils.MyLog;
import com.clyr.utils.PublicTools;
import com.clyr.utils.bottomsmall.DiscvoerEssaySpacingItemDecoration;
import com.clyr.utils.bottomsmall.ListRvAdapter;
import com.clyr.utils.bottomsmall.PhotoRvAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 11635
 */
public class BottomSmallActivity extends BaseActivity {
    RecyclerView recyclerview;
    RecyclerView list_otherjobs;

    LinearLayout frame_list, lin_title;
    private int topInt;
    private int centerInt;
    private int heightInt;
    private View default_pop;

    private float sx;
    private float sy;
    private final List<Float> ml = new ArrayList<>();

    enum State {
        /**
         * 显示状态
         */
        SHOW,
        /**
         * 收起状态
         */
        HIDE
    }

    private int mDy = 0;
    private final int animaCount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_small);
    }

    @Override
    protected void initView() {
        initBar();

        recyclerview = findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addItemDecoration(new DiscvoerEssaySpacingItemDecoration(PublicTools.dip2px(this, 9.5F)));

        recyclerview.setAdapter(new PhotoRvAdapter(this, getImgList()));


        list_otherjobs = findViewById(R.id.list_otherjobs);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        list_otherjobs.setLayoutManager(layoutManager2);
        list_otherjobs.addItemDecoration(new DiscvoerEssaySpacingItemDecoration(PublicTools.dip2px(this, 0.5F)));

        list_otherjobs.setAdapter(new ListRvAdapter(this, getImgList()));

        frame_list = findViewById(R.id.frame_list);
        lin_title = findViewById(R.id.lin_title);
        default_pop = findViewById(R.id.default_pop);

        initViewTouch();


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViewTouch() {
        topInt = PublicTools.dip2px(this, 85);
        centerInt = PublicTools.getScreenWidth(this) >> 1;
        heightInt = PublicTools.getScreenHeight(this);

        frame_list.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // top = 260,getX = 0.0
                // bTop = 1739,location-x = 540,location-y = 1813
                // top = 260,getX = 540.0
                frame_list.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                MyLog.logeArr("top = " + frame_list.getTop(), "getX = " + frame_list.getX());

                hidePosition(false);
            }
        });

        lin_title.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sx = event.getRawX();
                    sy = event.getRawY();
                    ml.clear();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float y = event.getRawY();
                    float dx = x - sx;
                    float dy = y - sy;
                    ml.add(dy);
                    float x1 = frame_list.getX();
                    float y1 = frame_list.getY();
                    float t1 = y1 + dy;
                    int tTop = default_pop.getBottom() - lin_title.getHeight();
                    if (t1 <= topInt || t1 >= tTop) {
                        return false;
                    }
                    float x2 = getX(t1);

                    frame_list.setX(x2);
                    frame_list.setY(t1);
                    sx = x;
                    sy = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (getAcce() != 0) {
                        if (getAcce() == -1) {
                            showPosition();
                        } else {
                            hidePosition();
                        }
                    } else {
                        int xup = (int) event.getRawX();
                        int yup = (int) event.getRawY();
                        float lastx = frame_list.getX();
                        MyLog.loge(lastx + "");
                        if (lastx >= centerInt) {
                            showPosition();
                        } else {
                            hidePosition();
                        }

                        if (Math.abs(sx - xup) <= 0 && Math.abs(sy - yup) <= 0) {
                            if (getPostionState() == State.HIDE) {
                                showPosition();
                            } else {
                                hidePosition();
                            }
                        }
                    }
                    ml.clear();
                    break;
                default:
            }
            return true;
        });
    }

    private void hidePosition() {
        hidePosition(true);
    }

    private void hidePosition(boolean haveAnimation) {
        int bTop = default_pop.getBottom() - lin_title.getHeight();
        if (haveAnimation) {
            sy = frame_list.getY();
            moveAnimation(sy + animaCount, animaCount);
        } else {
            frame_list.setX(centerInt);
            frame_list.setY(bTop);
        }
        MyLog.logeArr("top = " + frame_list.getTop(), "getX = " + frame_list.getX());
    }

    private void showPosition() {
        showPosition(true);
    }

    private void showPosition(boolean haveAnimation) {
        if (haveAnimation) {
            sy = frame_list.getY();
            moveAnimation(sy - animaCount, -animaCount);
        } else {
            frame_list.setX(0);
            frame_list.setY(topInt);
        }
    }

    Handler handler = new Handler(Looper.myLooper());

    private void moveAnimation(float distance, float anCount) {
        float dy = distance - sy;
        float t1 = frame_list.getY() + dy;

        int tTop = default_pop.getBottom() - lin_title.getHeight();
        if (t1 <= topInt || t1 >= tTop) {
            if (t1 <= topInt) {
                showPosition(false);
            } else {
                hidePosition(false);
            }
            return;
        }
        float x1 = getX(t1);

        frame_list.setX(x1);
        frame_list.setY(t1);
        // 获取移动后的位置
        sy = distance;
        if (anCount > 0) {
            if (distance + anCount < tTop) {
                handler.postDelayed(() -> {
                    moveAnimation(distance + anCount, anCount);
                }, 1);
            } else {
                hidePosition(false);
            }
        } else {
            if (distance + anCount > topInt) {
                handler.postDelayed(() -> {
                    moveAnimation(distance + anCount, anCount);
                }, 1);
            } else {
                showPosition(false);
            }
        }
    }


    private int getAcce() {
        int iCount = 3;
        if (ml != null && ml.size() >= 2) {
            float dValue1 = ml.get(ml.size() - 1);
            float dValue2 = ml.get(ml.size() - 2);

            if (dValue2 >= 0 && dValue1 > 0) {
                return 1;
            } else if (dValue2 <= 0 && dValue1 < 0) {
                return -1;
            }
        }
        return 0;
    }

    private int[] getLocation() {
        int[] location = new int[2];
        frame_list.getLocationInWindow(location);
        MyLog.logeArr("location-x = " + location[0], "location-y = " + location[1]);
        int bTop = default_pop.getBottom() - lin_title.getHeight();

        return location;
    }

    private State getPostionState() {
        int bTop = default_pop.getBottom() - lin_title.getHeight();
        if (getLocation()[1] >= bTop) {
            return State.HIDE;
        } else {
            return State.SHOW;
        }
    }

    private float getX(float sy) {
        int icc = (frame_list.getHeight() - lin_title.getHeight()) * 100 / centerInt;
        if (icc <= 0) {
            return 10;
        }
        float iy = sy - topInt;
        /*float iyb = iy * 10000 / icc;
        return (iyb + 50) / 100;*/
        return iy * 100 / icc;
    }

    @NonNull
    private List<String> getImgList() {
        List<String> list = new ArrayList<>();
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c19c8eccde2.jpg");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c0ade5ba022.jpg");
        list.add("https://imghr.heiguang.net/2/2021/0520/2021052060a5c39806e3a2.png");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c25e687cc42.jpg");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c25f27278d2.jpg");
        list.add("https://imghr.heiguang.net/2/2021/0520/2021052060a5c37d2cb6d2.png");
        list.add("https://imghr.heiguang.net/2/2021/0520/2021052060a5c5c9d947f2.jpg");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c19c8eccde2.jpg");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c0ade5ba022.jpg");
        list.add("https://imghr.heiguang.net/2/2021/0520/2021052060a5c39806e3a2.png");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c25e687cc42.jpg");
        list.add("https://imghr.heiguang.net/2/2022/0324/20220324623c25f27278d2.jpg");
        list.add("https://imghr.heiguang.net/2/2021/0520/2021052060a5c37d2cb6d2.png");
        list.add("https://imghr.heiguang.net/2/2021/0520/2021052060a5c5c9d947f2.jpg");
        return list;
    }


}