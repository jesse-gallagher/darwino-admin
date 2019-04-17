package com.darwino.admin.app.dbtree;

import java.util.List;

import javax.enterprise.inject.spi.CDI;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import com.darwino.admin.DatabasesBean;
import com.darwino.config.jsonstore.JsonDbJdbc;

@SuppressWarnings("unused")
public class DatabaseTree extends Composite {
	TreeViewer databaseBrowser;
	private ResourceManager resourceManager;
	private Composite target;

	public DatabaseTree(Composite parent, ResourceManager resourceManager) {
		super(parent, SWT.NONE);
		this.resourceManager = resourceManager;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		createChildren();
	}
	
	public void setTarget(Composite target) {
		this.target = target;
	}

	@Override
	protected void checkSubclass() {
	}
	
	private void createChildren() {
		databaseBrowser = new TreeViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		databaseBrowser.setContentProvider(new TreeNodeContentProvider());
		databaseBrowser.setLabelProvider(new DatabasesTreeLabelProvider());
		
		Tree tree = databaseBrowser.getTree();
		Font font = tree.getFont();
		tree.setFont(resourceManager.createFont(FontDescriptor.createFrom(font.getFontData()[0].name, 10, SWT.NORMAL)));
		tree.setLinesVisible(false);
		
//		TreeViewerColumn col = new TreeViewerColumn(databaseBrowser, SWT.NONE);
		
		List<String> jsonDbNames = CDI.current().select(DatabasesBean.class).get().getJsonDbNames();
		databaseBrowser.setInput(
			jsonDbNames.stream()
				.map(JsonDbTreeNode::new)
				.toArray(TreeNode[]::new)
		);
	}
}
