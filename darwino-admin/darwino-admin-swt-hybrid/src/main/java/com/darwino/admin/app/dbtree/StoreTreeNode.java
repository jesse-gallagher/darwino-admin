package com.darwino.admin.app.dbtree;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.darwino.admin.app.AppShell;
import com.darwino.admin.app.SwtMainClass;
import com.darwino.admin.app.info.StoreInfoPane;
import com.darwino.jsonstore.Database;
import com.darwino.jsonstore.Store;

import lombok.SneakyThrows;

public class StoreTreeNode extends DBListTreeNode {
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
	
	@SneakyThrows
	public Store getStore() {
		return database.getStore(getStoreId());
	}

	@Override
	public Image getImage() {
		if(Database.STORE_LOCAL.equals(getStoreId())) {
			return AppShell.resourceManager.createImage(SwtMainClass.IMAGE_STORE_LOCAL);
		} else {
			return AppShell.resourceManager.createImage(SwtMainClass.IMAGE_STORE);
		}
	}
	
	@Override
	public void displayInfoPane(Composite target) {
		super.displayInfoPane(target);
		
		new StoreInfoPane(target, getStore());
		
		target.layout();
	}
}
