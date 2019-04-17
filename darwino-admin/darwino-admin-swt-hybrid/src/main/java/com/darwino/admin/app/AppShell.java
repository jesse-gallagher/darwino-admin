package com.darwino.admin.app;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.darwino.admin.app.tree.DatabaseTree;

public class AppShell extends Shell {
	private DatabaseTree databaseBrowser;
	private ResourceManager resourceManager;
	
	public AppShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		setSize(639, 498);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		this.resourceManager = new LocalResourceManager(JFaceResources.getResources(), this);
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
	
		databaseBrowser = new DatabaseTree(sashForm, resourceManager);
		
		Composite infoPane = new Composite(sashForm, SWT.NONE);

		sashForm.setWeights(new int[] { 1, 3 });
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
