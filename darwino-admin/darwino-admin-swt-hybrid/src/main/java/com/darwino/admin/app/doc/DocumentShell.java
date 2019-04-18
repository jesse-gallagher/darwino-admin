package com.darwino.admin.app.doc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.darwino.admin.Messages;
import com.darwino.admin.app.info.DocumentInfoPane;
import com.darwino.jsonstore.Document;

import lombok.SneakyThrows;

public class DocumentShell extends Shell {
	private final Document doc;
	
	private Listener closeListener = event -> {
		if(isDisposed()) { return; }
		if(getDisplay().getActiveShell() == this && event.stateMask == SWT.COMMAND && event.keyCode == 'w') {
			close();
		}
	};
	
	@SneakyThrows
	public DocumentShell(Display display, Document doc) {
		super(display);
		this.doc = doc;
		
		setText("Document " + doc.getUnid() + " - " + Messages.getString("appName"));
		
		setLayout(new FillLayout());
		setSize(1024, 768);
		
		new DocumentInfoPane(this, doc);
		
		layout();
		
		display.addFilter(SWT.KeyDown, this.closeListener);
	}
	
	@Override
	protected void checkSubclass() {
		
	}

	@Override
	public void close() {
		getDisplay().removeFilter(SWT.KeyDown, this.closeListener);
		super.close();
	}
}
