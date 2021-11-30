package com.clyr.view.treelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.clyr.view.R;

import java.util.ArrayList;
import java.util.List;

public class TreeAdapter extends BaseAdapter{
	private Context con;
	private LayoutInflater lif;
	private List<Node> all = new ArrayList<Node>();//展示
	private List<Node> cache = new ArrayList<Node>();//缓存
	private TreeAdapter tree = this;
	boolean hasCheckBox;
	private int expandIcon = -1;//展开图标
	private int collapseIcon = -1;//收缩图标
	/**
	 * 构造方法
	 */
	public TreeAdapter(Context context,List<Node>rootNodes){
		this.con = context;
		this.lif = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i=0;i<rootNodes.size();i++){
			addNode(rootNodes.get(i));
		}
	}
	/**
	 * 把一个节点上的所有的内容都挂上去
	 * @param node
	 *
	 */
	public void addNode(Node node){
		all.add(node);
		cache.add(node);
		if(node.isLeaf())return;
		for(int i = 0;i<node.getChildrens().size();i++){
			addNode(node.getChildrens().get(i));
		}
	}
	/**
	 * 设置展开收缩图标
	 * @param expandIcon
	 * @param collapseIcon
	 *
	 */
	public void setCollapseAndExpandIcon(int expandIcon,int collapseIcon){
		this.collapseIcon = collapseIcon;
		this.expandIcon = expandIcon;
	}
	/**
	 * 一次性对某节点的所有节点进行选中or取消操作
	 *
	 *
	 */
	public void checkNode(Node n,boolean isChecked){
		checkParentNode(n, isChecked);
		for(int i =0 ;i<n.getChildrens().size();i++){
			checkNode((Node)n.getChildrens().get(i), isChecked);
		}
	}

	public void checkParentNode(Node n, boolean isChecked){
		n.setChecked(isChecked);
		Node parent = n.getParent();
		if (parent != null) {
			if(isChecked){
				checkParentNode(parent, true);
			}else{
				boolean checked = false;
				for (int i = 0; i < parent.getChildrens().size(); i++) {
					Node child = parent.getChildrens().get(i);
					if(child.isChecked()) {
						checked = true;
						break;
					}
				}
				checkParentNode(parent, checked);
			}
		}
	}

	/**
	 * 获取所有选中节点
	 * @return
	 *
	 */
	public List<Node>getSelectedNode(){
		List<Node>checks =new ArrayList<Node>()	;
		for(int i = 0;i<cache.size();i++){
			Node n =(Node)cache.get(i);
			if(n.isChecked())
				checks.add(n);
		}
		return checks;
	}
	/**
	 * 设置是否有复选框
	 * @param hasCheckBox
	 *
	 */
	public void setCheckBox(boolean hasCheckBox){
		this.hasCheckBox = hasCheckBox;
	}
	/**
	 * 控制展开缩放某节点
	 * @param location
	 *
	 */
	public void ExpandOrCollapse(int location){
		Node n = all.get(location);//获得当前视图需要处理的节点
		if(n!=null)//排除传入参数错误异常
		{
			if(!n.isLeaf()){
				n.setExplaned(!n.isExplaned());// 由于该方法是用来控制展开和收缩的，所以取反即可
				filterNode();//遍历一下，将所有上级节点展开的节点重新挂上去
				this.notifyDataSetChanged();//刷新视图
			}
		}

	}
	/**
	 * 设置展开等级
	 * @param level
	 *
	 */
	public void setExpandLevel(int level){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(n.getLevel()<=level){
				if(n.getLevel()<level)
					n.setExplaned(true);
				else
					n.setExplaned(false);
				all.add(n);
			}
		}

	}
	/* 清理all,从缓存中将所有父节点不为收缩状态的都挂上去*/
	public void filterNode(){
		all.clear();
		for(int i = 0;i<cache.size();i++){
			Node n = cache.get(i);
			if(!n.isParentCollapsed()||n.isRoot())//凡是父节点不收缩或者不是根节点的都挂上去
				all.add(n);
		}
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return all.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int location) {
		// TODO Auto-generated method stub
		return all.get(location);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int location) {
		// TODO Auto-generated method stub
		return location;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int location, View view, ViewGroup viewgroup) {

		ViewItem vi = null;
		if(view == null){
			view = lif.inflate(R.layout.list_item, null);
			vi = new ViewItem();
//			vi.cb = (CheckBox)view.findViewById( R.id.cb);
			vi.flagIcon = (ImageView)view.findViewById(R.id.ivec);
			vi.tv = (TextView)view.findViewById(R.id.itemvalue);
//			vi.icon =(ImageView)view.findViewById(R.id.ivicon);
//			vi.cb.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					Node n = (Node)v.getTag();
//					checkNode(n, ((CheckBox)v).isChecked());
//					tree.notifyDataSetChanged();
//				}
//			});
			view.setTag(vi);
		}
		else{
			vi = (ViewItem)view.getTag();
			if(vi ==null)
				System.out.println();
		}
		Node n = all.get(location);
		if(n!=null){
//			if(vi==null||vi.cb==null)
//				System.out.println();
//			vi.cb.setTag(n);
//			vi.cb.setChecked(n.isChecked());
			//叶节点不显示展开收缩图标
			if(n.isLeaf()){
				vi.flagIcon.setVisibility(View.GONE);
			}
			else{
				vi.flagIcon.setVisibility(View.VISIBLE);
				vi.flagIcon.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ExpandOrCollapse(location);
					}
				});
				if(n.isExplaned()){
					if(expandIcon!=-1){
						vi.flagIcon.setImageResource(expandIcon);
					}
				}
				else{
					if(collapseIcon!=-1){
						vi.flagIcon.setImageResource(collapseIcon);
					}
				}
			}
//			//设置是否显示复选框
//			if(n.hasCheckBox()&&n.hasCheckBox()){
//				vi.cb.setVisibility(View.VISIBLE);
//			}
//			else{
//				vi.cb.setVisibility(View.GONE);
//			}
			//设置是否显示头像图标
//			if(n.getIcon()!=-1){
//				vi.icon.setImageResource(n.getIcon());
//				vi.icon.setVisibility(View.VISIBLE);
//			}
//			else{
//				vi.icon.setVisibility(View.GONE);
//			}
			//显示文本
			vi.tv.setText(n.getTitle());
			// 控制缩进
			view.setPadding(30*n.getLevel(), 3,3, 3);
		}
		return view;
	}
	public class ViewItem{
		private CheckBox cb;
		private ImageView icon;
		private ImageView flagIcon;
		private TextView tv;
	}
}
