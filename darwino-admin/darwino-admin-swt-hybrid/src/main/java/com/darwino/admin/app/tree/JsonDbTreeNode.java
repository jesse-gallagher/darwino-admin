package com.darwino.admin.app.tree;

import org.eclipse.jface.viewers.TreeNode;

public class JsonDbTreeNode extends TreeNode {

	public JsonDbTreeNode(String beanName) {
		super(beanName);
	}
	
	public String getBeanName() {
		return (String)getValue();
	}

	@Override
	public String toString() {
		return getBeanName();
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public TreeNode[] getChildren() {
		return new TreeNode[0];
	}
}
