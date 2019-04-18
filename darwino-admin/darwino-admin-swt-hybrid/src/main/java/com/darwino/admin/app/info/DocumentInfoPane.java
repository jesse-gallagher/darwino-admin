package com.darwino.admin.app.info;

import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.darwino.commons.util.StringUtil;
import com.darwino.jsonstore.Document;

import lombok.SneakyThrows;

public class DocumentInfoPane extends AbstractInfoPane {

	private final Document doc;
	
	@SneakyThrows
	public DocumentInfoPane(Composite parent, Document doc) {
		super(parent, "Document");
		this.doc = doc;
		
		info("UNID", doc.getUnid());
		info("Tags", getTags(doc));
		
		Text editor = new Text(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		// TODO pick a non-named font - https://stackoverflow.com/questions/221568/swt-os-agnostic-way-to-get-monospaced-font
		editor.setFont(new Font(parent.getDisplay(), "Consolas", 12, SWT.NORMAL)); //$NON-NLS-1$
		
		
		editor.setText(doc.getJsonString(false));
		layout();
		
		editor.clearSelection();
	}
	
	@SneakyThrows
	private String getTags(Document doc) {
		Object obj = doc.get(Document.SYSTEM_TAGS);
		if(obj instanceof CharSequence) {
			return obj.toString();
		} else if(obj instanceof Collection) {
			return ((Collection<?>)obj).stream()
				.map(StringUtil::toString)
				.collect(Collectors.joining(", "));
		} else {
			return ""; //$NON-NLS-1$
		}
	}
}
