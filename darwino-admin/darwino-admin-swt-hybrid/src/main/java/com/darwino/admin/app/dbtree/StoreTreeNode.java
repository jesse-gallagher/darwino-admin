package com.darwino.admin.app.dbtree;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;

import com.darwino.admin.app.AppShell;
import com.darwino.admin.app.SwtMainClass;
import com.darwino.jsonstore.Database;

public class StoreTreeNode extends TreeNode implements DBListTreeNode {
	private final Database database;

	public StoreTreeNode(Database database, String storeId) {
		super(storeId);
		this.database = database;
	}
	
	public String getStoreId() {
		return (String)getValue();
	}
	
	@Override
	public String toString() {
		return getStoreId();
	}
	
	public Database getDatabase() {
		return database;
	}

	@Override
	public Image getImage() {
		if(Database.STORE_LOCAL.equals(getStoreId())) {
			return AppShell.resourceManager.createImage(SwtMainClass.IMAGE_STORE_LOCAL);
		} else {
			return AppShell.resourceManager.createImage(SwtMainClass.IMAGE_STORE);
		}
	}
}
