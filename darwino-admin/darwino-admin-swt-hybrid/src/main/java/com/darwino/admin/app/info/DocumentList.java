package com.darwino.admin.app.info;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.darwino.admin.app.doc.DocumentShell;
import com.darwino.commons.json.JsonException;
import com.darwino.commons.json.JsonObject;
import com.darwino.commons.util.StringUtil;
import com.darwino.jsonstore.Store;
import com.darwino.jsonstore.callback.CursorEntry;

import lombok.SneakyThrows;

public class DocumentList extends Composite {
	
	private TableViewer docs;
	private final Store store;

	public DocumentList(Composite parent, Store store) {
		super(parent, SWT.NONE);
		setLayout(new FillLayout());
		this.store = store;
		
		createChildren();
		connectActions();
		
		layout();
	}
	
	private void createChildren() {
		this.docs = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		this.docs.setContentProvider(new DocumentListContentProvider(this.docs, store));
		this.docs.setUseHashlookup(true);
		
		Table table = this.docs.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		FontDescriptor tableFont = FontDescriptor.createFrom(table.getFont()).increaseHeight(-2);
		table.setFont(tableFont.createFont(this.getDisplay()));
		
		TableViewerColumn unidCol = new TableViewerColumn(this.docs, SWT.NONE);
		unidCol.getColumn().setText("UNID");
		unidCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			@SneakyThrows
			public void update(ViewerCell cell) {
				CursorEntry entry = (CursorEntry)cell.getElement();
				cell.setText(entry.getUnid());
			}
		});
		unidCol.getColumn().setWidth(250);

		
		TableViewerColumn createdCol = new TableViewerColumn(this.docs, SWT.NONE);
		createdCol.getColumn().setText("Created");
		createdCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			@SneakyThrows
			public void update(ViewerCell cell) {
				CursorEntry entry = (CursorEntry)cell.getElement();
				cell.setText(StringUtil.toString(entry.getCreationDate()));
			}
		});
		createdCol.getColumn().setWidth(250);

		TableViewerColumn jsonCol = new TableViewerColumn(this.docs, SWT.NONE);
		jsonCol.getColumn().setText("JSON");
		jsonCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			@SneakyThrows
			public void update(ViewerCell cell) {
				CursorEntry entry = (CursorEntry)cell.getElement();
				JsonObject json = (JsonObject)entry.getJson();
				cell.setText(json.toJson(true));
			}
		});
		jsonCol.getColumn().setWidth(800);
	}
	
	private void connectActions() {
		this.docs.addDoubleClickListener(event -> {
			CursorEntry entry = (CursorEntry)this.docs.getStructuredSelection().getFirstElement();
			
			try {
				DocumentShell shell = new DocumentShell(getDisplay(), entry.loadDocument());
				shell.open();
				shell.layout();
			} catch (JsonException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
