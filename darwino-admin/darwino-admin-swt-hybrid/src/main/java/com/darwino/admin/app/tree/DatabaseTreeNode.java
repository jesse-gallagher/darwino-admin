package com.darwino.admin.app.tree;

import org.eclipse.jface.viewers.TreeNode;

import com.darwino.jsonstore.sql.impl.full.LocalFullJsonDBServerImpl;

public class DatabaseTreeNode extends TreeNode {

	public DatabaseTreeNode(LocalFullJsonDBServerImpl server, String databaseName) {
		super(databaseName);
	}
	
	public String getDatabaseName() {
		return (String)getValue();
	}

	@Override
	public String toString() {
		return getDatabaseName();
	}
}
