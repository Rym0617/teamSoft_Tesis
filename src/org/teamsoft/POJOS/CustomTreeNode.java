package org.teamsoft.POJOS;

public class CustomTreeNode {
	
	private Object object;
	private String name;
	
	
	public CustomTreeNode() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomTreeNode(Object object, String name) {
		super();
		this.object = object;
		this.name = name;
	}
	
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
