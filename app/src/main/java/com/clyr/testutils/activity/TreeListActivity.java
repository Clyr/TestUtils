package com.clyr.testutils.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.view.treelist.NodeResource;
import com.clyr.view.treelist.TreeListView;

import java.util.ArrayList;
import java.util.List;

public class TreeListActivity extends BaseActivity {
    private TreeListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_list);
    }

    @Override
    protected void initView() {
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        listView = new TreeListView(this, initNodeTree2());
        rl.addView(listView);
        setContentView(rl);
    }

    public List<NodeResource> initNodeTree() {
        List<NodeResource> list = new ArrayList<NodeResource>();
        NodeResource n1 = new NodeResource("", "" + 0, "根节点,自己是0", "dfs", R.drawable.icon_department);
        list.add(n1);
        NodeResource n2 = new NodeResource(null, "" + 4, "根节点，自己是4", "dfs", R.drawable.icon_department);
        list.add(n2);
        NodeResource n3 = new NodeResource("" + 0, "" + 7, "父id是0，自己是7", "dfs", R.drawable.icon_department);
        list.add(n3);
        NodeResource n4 = new NodeResource("" + 7, "" + 10, "父id是7，自己是10", "dfs", R.drawable.icon_department);
        list.add(n4);
        NodeResource n5 = new NodeResource("" + 7, "" + 14, "父id是7，自己是14", "dfs", R.drawable.icon_department);
        list.add(n5);
        NodeResource n6 = new NodeResource("" + 10, "" + 18, "父id是10，自己是18", "dfs", R.drawable.icon_department);
        list.add(n6);
        NodeResource n7 = new NodeResource("" + 4, "" + 22, "父id是4，自己是22", "dfs", R.drawable.icon_department);
        list.add(n7);

        NodeResource n8 = new NodeResource("" + 0, "" + 1, "父id是0，自己是1", "dfs", R.drawable.icon_department);
        list.add(n8);
        NodeResource n9 = new NodeResource("" + -1, "" + 1, "父id是0，自己是1", "dfs", R.drawable.icon_department);
        list.add(n9);
        return list;
    }

    public List<NodeResource> initNodeTree2() {
        List<NodeResource> list = new ArrayList<NodeResource>();
        NodeResource n1 = new NodeResource("-1", "" + 0, "根节点,自己是0", "dfs", R.drawable.icon_department);
        list.add(n1);
        NodeResource n2 = new NodeResource("-1", "" + 1, "根节点，自己是1", "dfs", R.drawable.icon_department);
        list.add(n2);
        NodeResource n3 = new NodeResource("-1", "" + 2, "根节点，自己是2", "dfs", R.drawable.icon_department);
        list.add(n3);
        NodeResource n4 = new NodeResource("-1", "" + 3, "根节点，自己是3", "dfs", R.drawable.icon_department);
        list.add(n4);
        NodeResource n5 = new NodeResource("0", "" + 5, "根节点，自己是4", "dfs", R.drawable.icon_department);
        list.add(n5);
        NodeResource n6 = new NodeResource("1", "" + 6, "根节点，自己是5", "dfs", R.drawable.icon_department);
        list.add(n6);
        NodeResource n7 = new NodeResource("6", "" + 7, "根节点，自己是6", "dfs", R.drawable.icon_department);
        list.add(n7);
        return list;
    }
}