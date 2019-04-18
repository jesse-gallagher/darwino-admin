package com.darwino.admin.app.dbtree;

import java.util.Arrays;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

abstract class DBListTreeNode extends TreeNode {
	public DBListTreeNode(Object value) {
		super(value);
	}

	public abstract Image getImage();
	
	public void displayInfoPane(Composite target) {
		Arrays.stream(target.getChildren()).forEach(Control::dispose);
	}
}
