package com.darwino.admin.app.doc;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.darwino.admin.Messages;
import com.darwino.admin.app.info.DocumentInfoPane;
import com.darwino.commons.json.JsonException;
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
		
		setLayout(new GridLayout(2, false));
		setSize(1024, 768);
		
		DocumentInfoPane docInfo = new DocumentInfoPane(this, doc);
		docInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Composite actionSpacer = new Composite(this, SWT.NONE);
		actionSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		Composite actions = new Composite(this, SWT.NONE);
		actionSpacer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		actions.setLayout(new GridLayout());
		
		Button save = new Button(actions, SWT.PUSH);
		save.setText("Save");
		save.addListener(SWT.Selection, evt -> {
			try {
				this.doc.setJsonString(docInfo.getJsonString());
				this.doc.save();
				close();
			} catch(JsonException e) {
				MessageDialog.openError(this, "Error Saving Document", e.getMessage());
			}
		});
		Button cancel = new Button(actions, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.addListener(SWT.Selection, evt -> close());
		
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
