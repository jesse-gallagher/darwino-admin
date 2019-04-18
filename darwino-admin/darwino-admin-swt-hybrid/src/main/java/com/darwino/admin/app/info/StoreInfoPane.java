package com.darwino.admin.app.info;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.darwino.jsonstore.Store;

import lombok.SneakyThrows;

public class StoreInfoPane extends AbstractInfoPane {

	private final Store store;
	
	public StoreInfoPane(Composite parent, Store store) {
		super(parent, "Store");
		this.store = store;
		
		createChildren();
	}
	
	@SneakyThrows
	protected void createChildren() {
		info("Name", store.getId());
		info("Documents", store.documentCount());
		
		DocumentList docList = new DocumentList(this, store);
		docList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}

}
