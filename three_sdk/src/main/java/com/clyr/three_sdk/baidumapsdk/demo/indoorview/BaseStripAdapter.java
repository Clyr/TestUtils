/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.indoorview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 楼层条数据适配器
 */
public class BaseStripAdapter extends BaseAdapter {

    private List<String> mFloorList = new ArrayList<String>();
    private int selectedPos;
    private Context mContext;

    private class NoteViewHolder {

        private TextView mFloorTextTV;
    }

    public BaseStripAdapter(Context ctx) {
        mContext = ctx;
    }

    public void setmFloorList(List<String> mFloorList) {
        this.mFloorList = mFloorList;
    }

    public void setNoteList(List<String> floorList) {
        mFloorList = floorList;
    }

    public int getCount() {
        return mFloorList.size();
    }

    public Object getItem(int position) {
        return mFloorList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void setSelectedPostion(int postion) {
        selectedPos = postion;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        NoteViewHolder holder;
        if (convertView == null) {
            convertView = new StripItem(mContext);
            holder = new NoteViewHolder();
            holder.mFloorTextTV = ((StripItem) convertView).getText();
            convertView.setTag(holder);
        } else {
            holder = (NoteViewHolder) convertView.getTag();
        }

        String floor = mFloorList.get(position);
        if (floor != null) {
            holder.mFloorTextTV.setText(floor);
        }
        if (selectedPos == position) {
            refreshViewStyle(holder.mFloorTextTV, true);
        } else {
            refreshViewStyle(holder.mFloorTextTV, false);
        }
        return convertView;
    }

    private void refreshViewStyle(TextView view, boolean isSelected) {
        if (isSelected) {
            view.setBackgroundColor(StripItem.colorSelected);
        } else {
            view.setBackgroundColor(StripItem.color);
        }
        view.setSelected(isSelected);
    }
}

