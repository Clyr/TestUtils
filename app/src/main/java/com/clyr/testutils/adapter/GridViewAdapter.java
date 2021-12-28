package com.clyr.testutils.adapter;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

import com.clyr.base.interfaces.OnItemClickListener;
import com.clyr.testutils.R;
import com.clyr.testutils.activity.GridActivity;
import com.clyr.view.MyVideoView;
import com.clyr.view.ProcessImageView;

/**
 * Created by 11635 of clyr on 2021/11/30.
 */
public class GridViewAdapter extends BaseAdapter {
    private final GridActivity gridActivity;
    private final LayoutInflater mInflater;
    private final OnItemClickListener listener;

    public GridViewAdapter(GridActivity gridActivity) {
        this.gridActivity = gridActivity;
        this.mInflater = LayoutInflater.from(gridActivity);
        this.listener = gridActivity;
    }

    @Override
    public int getCount() {
        if (gridActivity.mList != null) {
            return gridActivity.mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (gridActivity.mList != null) {
            return gridActivity.mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        if (gridActivity.mList != null) {
            if ("image".equals(gridActivity.mList.get(position).getTag())) {
                convertView = mInflater.inflate(R.layout.layout_image,
                        null);
                holder.image = convertView
                        .findViewById(R.id.image);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                holder.image.setImageBitmap(BitmapFactory.decodeFile(gridActivity.mList.get(position).getInfo(), options));
                holder.image.setOnClickListener(v -> listener.onClick(position, gridActivity.mList.get(position)));
                holder.image.setOnLongClickListener(v -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(gridActivity);
                    alert.setTitle("温馨提示").setMessage("是否要删除该图片？").setPositiveButton("确定", (dialog, which) -> {
                        if (gridActivity.mList != null && gridActivity.mList.size() >= position) {
                            gridActivity.mList.remove(position);
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton("取消", (dialog, which) -> {

                    }).show();
                    return false;
                });
            } else if ("video".equals(gridActivity.mList.get(position).getTag())) {
                convertView = mInflater.inflate(R.layout.layout_video,
                        null);
                holder.video = convertView
                        .findViewById(R.id.video);
                Uri uri = Uri.parse(gridActivity.mList.get(position).getInfo());
                final MyVideoView videoView = holder.video;
                videoView.setOnCompletionListener(mp -> videoView.start());
                //设置视频路径
                holder.video.setVideoURI(uri);
                //开始播放视频
                holder.video.start();
                holder.videoRe = convertView.findViewById(R.id.videolin);
                holder.videoRe.setOnClickListener(v -> {
                    AlertDialog.Builder alert = new AlertDialog.Builder(gridActivity);
                    alert.setTitle("温馨提示").setMessage("是否要删除该视频？").setPositiveButton("确定", (dialog, which) -> {
                        if (gridActivity.mList != null && gridActivity.mList.size() >= position) {
                            gridActivity.mList.remove(position);
                            notifyDataSetChanged();
                        }
                    }).setNegativeButton("取消", (dialog, which) -> {

                    }).show();
                });
                holder.video.setOnClickListener(v -> listener.onClick(position, gridActivity.mList.get(position)));
            } else if ("out".equals(gridActivity.mList.get(position).getTag())) {
                convertView = mInflater.inflate(R.layout.layout_image,
                        null);
                holder.image = convertView
                        .findViewById(R.id.image);
                holder.image.setImageResource(R.drawable.icon_addpic_unfocused);
                holder.image.setOnClickListener(v -> listener.onClick(-1, "ADD"));
                holder.image.setProgress(100);
            }
        }
        return convertView;
    }

    class ViewHolder {
        public ProcessImageView image;
        public MyVideoView video;
        public RelativeLayout videoRe;
        Button mButton;

    }
}
