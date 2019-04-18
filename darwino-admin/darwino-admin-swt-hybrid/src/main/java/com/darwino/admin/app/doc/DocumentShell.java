package com.darwino.admin.app.doc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.darwino.admin.Messages;
import com.darwino.jsonstore.Document;

import lombok.SneakyThrows;

public class DocumentShell extends Shell {
	private final Document doc;
	
	private Listener closeListener = event -> {
		if(getDisplay().getActiveShell() == this) {
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
		
		Text editor = new Text(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		// TODO pick a non-named font - https://stackoverflow.com/questions/221568/swt-os-agnostic-way-to-get-monospaced-font
		editor.setFont(new Font(display, "Consolas", 12, SWT.NORMAL)); //$NON-NLS-1$
		
		
		editor.setText(doc.getJsonString(false));
		
		layout();
		
		display.addFilter(SWT.KeyDown, this.closeListener);
		
		editor.clearSelection();
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
