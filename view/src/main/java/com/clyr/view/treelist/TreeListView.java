package com.clyr.view.treelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.clyr.view.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeListView extends ListView {
	ListView treelist = null;
	TreeAdapter ta = null;

	@SuppressLint("WrongConstant")
	public TreeListView(Context context, List<NodeResource> res) {
		super(context);
		treelist = this;
		treelist.setFocusable(false);
		treelist.setFadingEdgeLength(0);
		treelist.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		treelist.setDrawSelectorOnTop(false);
		treelist.setCacheColorHint(0xffffff);
		treelist.setDivider(getResources().getDrawable(R.drawable.divider_list));
		treelist.setDividerHeight(2);
		treelist.setFastScrollEnabled(true);
		treelist.setScrollBarStyle(NO_ID);
		treelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("print", "被点击"+position);
//				Toast.makeText(getContext(),position,Toast.LENGTH_SHORT).show();
//				((TreeAdapter) parent.getAdapter()).ExpandOrCollapse(position);
				Node item = (Node) ta.getItem(position);

			}
		});
		initNode(context, initNodRoot(res), true, -1, -1, 0);
	}

	/**
	 * 
	 * /@param context
	 *            响应监听的上下文
	 * /@param root
	 *            已经挂好树的根节点
	 * /@param hasCheckBox
	 *            是否整个树有复选框
	 * /@param tree_ex_id
	 *            展开iconid -1会使用默认的
	 * /@param tree_ec_id
	 *            收缩iconid -1会使用默认的
	 * /@param expandLevel
	 *            初始展开等级
	 * 
	 */
	public List<Node> initNodRoot(List<NodeResource> res) {
		ArrayList<Node> list = new ArrayList<Node>();
		ArrayList<Node> roots = new ArrayList<Node>();
		Map<String, Node> nodemap = new HashMap<String, Node>();
		for (int i = 0; i < res.size(); i++) {
			NodeResource nr = res.get(i);
			Node n = new Node(nr.title, nr.value, nr.parentId, nr.curId,
					nr.iconId);
			nodemap.put(n.getCurId(), n);// 生成map树
		}
		Set<String> set = nodemap.keySet();
		Collection<Node> collections = nodemap.values();
		Iterator<Node> iterator = collections.iterator();
		while (iterator.hasNext()) {// 添加所有根节点到root中
			Node n = iterator.next();
			if (!set.contains(n.getParentId()))
				roots.add(n);
			list.add(n);
		}
		for (int i = 0; i < list.size(); i++) {
			Node n = list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				Node m = list.get(j);
				if (m.getParentId() .equals ( n.getCurId() ) ) {
					n.addNode (m);
					m.setParent ( n );
				} else if (m.getCurId() .equals ( n.getParentId () ) ) {
					m.addNode(n);
					n.setParent(m);
				}
			}
		}
		return roots;
	}

	public void initNode(Context context, List<Node> root, boolean hasCheckBox,
			int tree_ex_id, int tree_ec_id, int expandLevel) {
		ta = new TreeAdapter(context, root);
		// 设置整个树是否显示复选框
		ta.setCheckBox(true);
		// 设置展开和折叠时图标
		// ta.setCollapseAndExpandIcon(R.drawable.tree_ex, R.drawable.tree_ec);
		int tree_ex_id_ = (tree_ex_id == -1) ? R.drawable.tree_ex : tree_ex_id;
		int tree_ec_id_ = (tree_ec_id == -1) ? R.drawable.tree_ec : tree_ec_id;
		ta.setCollapseAndExpandIcon(tree_ex_id_, tree_ec_id_);
		// 设置默认展开级别
		ta.setExpandLevel(expandLevel);
		this.setAdapter(ta);
	}

	/* 返回当前所有选中节点的List数组 */
	public List<Node> get() {
		return ta.getSelectedNode();
	}

}
