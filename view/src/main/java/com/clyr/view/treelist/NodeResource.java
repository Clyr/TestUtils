package com.clyr.view.treelist;
public class NodeResource {
	protected String parentId;
	protected String title;
	protected String value;
	protected int iconId;
	protected String curId;

	public NodeResource(){
	}
	
	
	public NodeResource(String parentId, String curId, String title,
			String value, int iconId) {
		super();
		this.parentId = parentId;
		this.title = title;
		this.value = value;
		this.iconId = iconId;
		this.curId = curId;
	}


	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public int getIconId() {
		return iconId;
	}


	public void setIconId(int iconId) {
		this.iconId = iconId;
	}


	public String getCurId() {
		return curId;
	}


	public void setCurId(String curId) {
		this.curId = curId;
	}

	

}
