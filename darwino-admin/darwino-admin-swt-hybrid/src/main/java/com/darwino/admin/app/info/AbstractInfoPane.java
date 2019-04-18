package com.darwino.admin.app.info;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.darwino.commons.util.StringUtil;

public abstract class AbstractInfoPane extends Composite {

	public AbstractInfoPane(Composite parent, String title) {
		super(parent, SWT.NONE);
		
		setLayout(new GridLayout(2, false));
		
		if(StringUtil.isNotEmpty(title)) {
			Label titleLabel = new Label(this, SWT.NONE);
			titleLabel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
			FontDescriptor titleFont = FontDescriptor.createFrom(titleLabel.getFont())
				.setStyle(SWT.BOLD)
				.increaseHeight(2);
			titleLabel.setFont(titleFont.createFont(titleLabel.getDisplay()));
			titleLabel.setText(title);
			
			Label separator = new Label(this, SWT.HORIZONTAL | SWT.SEPARATOR);
			separator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		}
	}

	protected void info(String labelText, Object value) {
		Label label = new Label(this, SWT.NONE);
		label.setText(labelText + (labelText.endsWith(":") ? "" : ":"));
		Label text = new Label(this, SWT.NONE);
		text.setText(StringUtil.toString(value));
		
	}
}
