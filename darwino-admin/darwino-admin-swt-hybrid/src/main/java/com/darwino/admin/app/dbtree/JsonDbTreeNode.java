package com.darwino.admin.app.dbtree;

import lombok.SneakyThrows;

import java.util.Arrays;

import javax.enterprise.inject.spi.CDI;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.darwino.admin.DatabasesBean;
import com.darwino.admin.app.AppShell;
import com.darwino.admin.app.SwtMainClass;
import com.darwino.admin.app.info.JsonDbInfoPane;
import com.darwino.commons.Platform;
import com.darwino.commons.util.StringUtil;
import com.darwino.config.jsonstore.JsonDbJdbc;
import com.darwino.jsonstore.sql.impl.full.JsonDb;
import com.darwino.jsonstore.sql.impl.full.LocalFullJsonDBServerImpl;

public class JsonDbTreeNode extends DBListTreeNode {
	private DatabasesBean databasesBean = CDI.current().select(DatabasesBean.class).get();
	private TreeNode[] children;

	public JsonDbTreeNode(String beanName) {
		super(beanName);
	}
	
	public String getBeanName() {
		return (String)getValue();
	}

	@Override
	public String toString() {
		JsonDb bean = getBean();
		if(bean instanceof JsonDbJdbc) {
			return StringUtil.format("{0} ({1})", getBeanName(), ((JsonDbJdbc)bean).getDb());	 //$NON-NLS-1$
		} else {
			return getBeanName();
		}
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	public JsonDb getBean() {
		return Platform.getManagedBean(JsonDb.BEAN_TYPE, getBeanName());
	}
	
	@Override
	@SneakyThrows
	public TreeNode[] getChildren() {
		if(this.children == null) {
			JsonDb bean = getBean();
			try {
				LocalFullJsonDBServerImpl server = databasesBean.getServerInstance(bean);
				
				this.children = Arrays.stream(server.getDatabaseList())
					.map(dbName -> new DatabaseTreeNode(server, dbName))
					.toArray(TreeNode[]::new);
			} catch(Throwable t) {
				Throwable c = t;
				while(c.getCause() != null) {
					c = c.getCause();
				}
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "Unable to List Databases", StringUtil.format("Encountered exception listing databases: {0}", c.getMessage()));
				this.children = new TreeNode[0];
			}
		}
		return this.children;
	}
	
	@Override
	public Image getImage() {
		return AppShell.resourceManager.createImage(SwtMainClass.IMAGE_SERVER);
	}
	
	@Override
	public void displayInfoPane(Composite target) {
		super.displayInfoPane(target);
		
		new JsonDbInfoPane(target, getBean());
		
		target.layout();
	}
}
