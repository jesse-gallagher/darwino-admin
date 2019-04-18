package com.darwino.admin.app.dbtree;

import java.util.Arrays;

import javax.enterprise.inject.spi.CDI;

import com.darwino.admin.DatabasesBean;
import com.darwino.admin.app.AppShell;
import com.darwino.admin.app.SwtMainClass;
import com.darwino.admin.app.info.DatabaseInfoPane;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.darwino.jsonstore.Database;
import com.darwino.jsonstore.Session;
import com.darwino.jsonstore.sql.impl.full.LocalFullJsonDBServerImpl;

import lombok.SneakyThrows;

public class DatabaseTreeNode extends DBListTreeNode {
	
	private final LocalFullJsonDBServerImpl server;
	private TreeNode[] children;

	public DatabaseTreeNode(LocalFullJsonDBServerImpl server, String databaseName) {
		super(databaseName);
		this.server = server;
	}
	
	public String getDatabaseName() {
		return (String)getValue();
	}

	@Override
	public String toString() {
		return getDatabaseName();
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	public Database getDatabase() {
		DatabasesBean databasesBean = CDI.current().select(DatabasesBean.class).get();
		Session session = databasesBean.getSession(server, null);
		return databasesBean.getDatabase(session, getDatabaseName());
	}
	
	@Override
	@SneakyThrows
	public synchronized TreeNode[] getChildren() {
		if(this.children == null) {
			Database database = getDatabase();
			return Arrays.stream(database.getStoreList())
				.sorted(String.CASE_INSENSITIVE_ORDER)
				.map(storeId -> new StoreTreeNode(database, storeId))
				.toArray(TreeNode[]::new);
		}
		return this.children;
	}
	
	@Override
	public Image getImage() {
		return AppShell.resourceManager.createImage(SwtMainClass.IMAGE_DATABASE);
	}
	
	@Override
	public void displayInfoPane(Composite target) {
		super.displayInfoPane(target);
		
		new DatabaseInfoPane(target, getDatabase());
		
		target.layout();
	}
}
