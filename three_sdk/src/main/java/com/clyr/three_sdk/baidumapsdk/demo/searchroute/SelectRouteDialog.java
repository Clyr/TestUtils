package com.clyr.three_sdk.baidumapsdk.demo.searchroute;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.search.core.RouteLine;
import com.clyr.three_sdk.R;

import java.util.List;



/**
 *  供路线选择的Dialog
 */
public class SelectRouteDialog extends Dialog {

    private List<? extends RouteLine> mRouteLines;
    private ListView mRouteListView;
    private RouteLineAdapter mRouteLineAdapter;
    public  OnItemInDlgClickListener onItemInDlgClickListener;

    public SelectRouteDialog(Context context, int theme) {
        super(context, theme);
    }

    public SelectRouteDialog(Context context, List<? extends RouteLine> routeLines, RouteLineAdapter.Type type) {
        this(context, 0);
        mRouteLines = routeLines;
        mRouteLineAdapter = new RouteLineAdapter(context, mRouteLines, type);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_dialog);
        mRouteListView = (ListView) findViewById(R.id.transitList);
        mRouteListView.setAdapter(mRouteLineAdapter);
        mRouteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemInDlgClickListener.onItemClick(position);
                dismiss();
            }
        });
    }

    public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
        onItemInDlgClickListener = itemListener;
    }

    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }
}
