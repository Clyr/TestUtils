package com.clyr.three_sdk.baidumapsdk.demo.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.clyr.three_sdk.R;


/**
 * Demo列表Adapter
 */

public class DemoListAdapter extends BaseAdapter {

    private DemoInfo[] demos;
    private Context mContext;

    public DemoListAdapter(Context context, DemoInfo[] demos ){
        super();
        this.demos = demos;
        this.mContext = context;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.demo_info_item, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView desc = (TextView) convertView.findViewById(R.id.desc);
        title.setText(demos[index].title);
        desc.setText(demos[index].desc);
        return convertView;
    }

    @Override
    public int getCount() {
        return demos.length;
    }

    @Override
    public Object getItem(int index) {
        return demos[index];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }
}
