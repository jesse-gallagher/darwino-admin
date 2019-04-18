package com.darwino.admin.app.dbtree;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.spi.CDI;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;

import com.darwino.admin.DatabasesBean;

public class DatabaseTree extends Composite {
	private TreeViewer databaseBrowser;
	private ResourceManager resourceManager;
	private Composite target;

	public DatabaseTree(Composite parent, ResourceManager resourceManager) {
		super(parent, SWT.NONE);
		this.resourceManager = resourceManager;
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		createChildren();
		connectActions();
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
		
		List<String> jsonDbNames = CDI.current().select(DatabasesBean.class).get().getJsonDbNames();
		databaseBrowser.setInput(
			jsonDbNames.stream()
				.map(JsonDbTreeNode::new)
				.toArray(TreeNode[]::new)
		);
	}
	
	private void connectActions() {
		databaseBrowser.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.item != null) {
					Object item = e.item.getData();
					if(item instanceof DBListTreeNode) {
						((DBListTreeNode)item).displayInfoPane(target);
					}
				} else {
					Arrays.stream(target.getChildren()).forEach(Control::dispose);
				}
			}
		});
	}
}
