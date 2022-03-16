package com.clyr.testutils.fragment.coolviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.clyr.base.interfaces.OnViewPagerListener;
import com.clyr.test.ScanActivity;
import com.clyr.test.view.MyViewPager;
import com.clyr.test.view.ScanPhotoAdapter;
import com.clyr.testutils.R;
import com.clyr.utils.MyLog;
import com.clyr.view.scrollablepanel.MyLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class MyInfoFragment extends Fragment {
    private final int[] imgs = {
            R.mipmap.img_video_1,
            R.mipmap.img_video_2,
            R.mipmap.img_video_3,
            R.mipmap.img_video_4,
            R.mipmap.img_video_5,
            R.mipmap.img_video_6,
            R.mipmap.img_video_7,
            R.mipmap.img_video_8
    };

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    MyLayoutManager myLayoutManager;
    private final List<Integer> mList = new ArrayList<>();

    private float touchX = 0;
    private float touchY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_my_info, container, false);
        initView(inflate);
        return inflate;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(View inflate) {
        mRecyclerView = inflate.findViewById(R.id.recyclerview);
        myLayoutManager = new MyLayoutManager(getContext(), OrientationHelper.VERTICAL, false);

        mAdapter = new MyAdapter(getContext(), mList);
        mRecyclerView.setLayoutManager(myLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        myLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {

            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                MyLog.loge("position = " + position);
                if (position + 2 >= mList.size()) {
                    addDatasToList(3);
                }
            }
        });
        addDatasToList(5);

        /*mRecyclerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchX = event.getX();
                    touchY = event.getY();
                    break;
                //case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (Math.abs(touchX - event.getX()) > Math.abs(touchY - event.getY())) {
                        MyLog.loge("X = " + Math.abs(touchX - event.getX())+" - Y = " + Math.abs(touchY - event.getY()));
                        return true;
                    }
                    break;
                default:
                    break;
            }
            return false;
        });*/
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getX();
                        touchY = event.getY();
                        break;
                    //case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(touchX - event.getX()) > Math.abs(touchY - event.getY())) {
                            MyLog.loge("X = " + Math.abs(touchX - event.getX()) + " - Y = " + Math.abs(touchY - event.getY()));
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addDatasToList(int count) {
        for (int i = 0; i < count; i++) {
            int icon = (int) (Math.random() * 8);
            mList.add(imgs[icon]);
        }
        mAdapter.notifyDataSetChanged();
        MyLog.loge("新增条数：" + count);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final Context mContext;
        private final List<Integer> list;
        boolean mLike = false;

        public MyAdapter(Context context, List<Integer> list) {
            this.mContext = context;
            this.list = list;
        }


        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

            List<String> images = new ArrayList<>();
            images.add("https://imgtuku.heiguang.net/photogallery/2022/02/17/u_1645077354_FQVIZCfM.jpg!c");
            images.add("https://imgtuku.heiguang.net/photogallery/2022/02/17/u_1645077354_wmSYcMew.jpg!c");
            holder.photoVp.setAdapter(new ScanPhotoAdapter(mContext, images, () -> {
                ScanActivity.show(mContext, images, 0);
                requireActivity().overridePendingTransition(com.clyr.test.R.anim.fade_in, com.clyr.test.R.anim.fade_out);
            }));

            holder.photoVp.setCurrentItem(0);
            /*holder.photoVp.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(touchX - event.getY()) > 100) {
                            MyLog.loge("横向滑动 - 100");
                            return false;
                        }
                        break;
                    default:
                        break;
                }
                return true;
            });*/
            holder.icon_like.setOnClickListener(v -> {
                if(mLike){
                    holder.icon_like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_heart_nolike));
                }else {
                    holder.icon_like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_heart_like));
                }
               mLike = !mLike;
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            MyViewPager photoVp;
            TextView titleTv;
            ImageView icon_like;

            public ViewHolder(View itemView) {
                super(itemView);
                photoVp = itemView.findViewById(R.id.vp_photo);
                titleTv = itemView.findViewById(R.id.tv_title);
                icon_like = itemView.findViewById(R.id.icon_like);
            }
        }
    }
}