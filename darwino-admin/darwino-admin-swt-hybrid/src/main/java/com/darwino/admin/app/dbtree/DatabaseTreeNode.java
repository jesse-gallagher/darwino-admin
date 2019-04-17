package com.darwino.admin.app.dbtree;

import java.util.Arrays;

import javax.enterprise.inject.spi.CDI;

import com.darwino.admin.DatabasesBean;
import com.darwino.admin.app.AppShell;
import com.darwino.admin.app.SwtMainClass;

import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;

import com.darwino.jsonstore.Database;
import com.darwino.jsonstore.Session;
import com.darwino.jsonstore.sql.impl.full.LocalFullJsonDBServerImpl;

import lombok.SneakyThrows;

public class DatabaseTreeNode extends TreeNode implements ImageTreeNode {
	
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
	
	@Override
	@SneakyThrows
	public synchronized TreeNode[] getChildren() {
		if(this.children == null) {
			DatabasesBean databasesBean = CDI.current().select(DatabasesBean.class).get();
			Session session = databasesBean.getSession(server, null);
			Database database = databasesBean.getDatabase(session, getDatabaseName());
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
}
