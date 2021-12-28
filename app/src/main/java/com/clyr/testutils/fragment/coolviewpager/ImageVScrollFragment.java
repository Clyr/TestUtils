package com.clyr.testutils.fragment.coolviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.clyr.base.interfaces.OnViewPagerListener;
import com.clyr.testutils.R;
import com.clyr.utils.MyLog;
import com.clyr.view.scrollablepanel.MyLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class ImageVScrollFragment extends Fragment {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_image_v_scroll, container, false);
        initView(inflate);
        return inflate;
    }

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

    class MyAdapter extends RecyclerView.Adapter<ImageVScrollFragment.MyAdapter.ViewHolder> {
        private final Context mContext;
        private final List<Integer> list;

        public MyAdapter(Context context, List<Integer> list) {
            this.mContext = context;
            this.list = list;
        }


        @NonNull
        @Override
        public ImageVScrollFragment.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_pager, parent, false);
            return new ImageVScrollFragment.MyAdapter.ViewHolder(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull ImageVScrollFragment.MyAdapter.ViewHolder holder, int position) {
            holder.img_thumb.setImageResource(list.get(position));

            holder.text_view.setText((position + 1) + "/" + list.size());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            RelativeLayout rootView;
            TextView text_view;

            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                rootView = itemView.findViewById(R.id.root_view);
                text_view = itemView.findViewById(R.id.text_view);
            }
        }
    }
}