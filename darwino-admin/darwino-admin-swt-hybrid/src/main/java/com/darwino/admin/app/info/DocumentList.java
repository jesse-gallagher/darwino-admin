package com.darwino.admin.app.info;

import java.text.DateFormat;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.darwino.admin.app.doc.DocumentShell;
import com.darwino.commons.json.JsonException;
import com.darwino.commons.json.JsonObject;
import com.darwino.commons.util.StringUtil;
import com.darwino.jsonstore.Cursor;
import com.darwino.jsonstore.Store;
import com.darwino.jsonstore.callback.CursorEntry;

import lombok.SneakyThrows;

public class DocumentList extends Composite {
	
	private static final ThreadLocal<DateFormat> DATE_FORMAT = ThreadLocal.withInitial(() -> DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
	
	private Text searchBox;
	private TableViewer docs;
	private TableViewerColumn unidCol;
	private TableViewerColumn createdCol;
	private final Store store;
	private String searchQuery;

	public DocumentList(Composite parent, Store store) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		this.store = store;
		
		createChildren();
		connectActions();
		
		layout();
	}
	
	@SneakyThrows
	private void createChildren() {
		
		this.searchBox = new Text(this, SWT.BORDER | SWT.SEARCH);
		this.searchBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.docs = new TableViewer(this, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.VIRTUAL);
		this.docs.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		resetDocList();
		this.docs.setUseHashlookup(true);
		
		Table table = this.docs.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		FontDescriptor tableFont = FontDescriptor.createFrom(table.getFont()).increaseHeight(-2);
		table.setFont(tableFont.createFont(this.getDisplay()));
		
		unidCol = new TableViewerColumn(this.docs, SWT.NONE);
		unidCol.getColumn().setText("UNID");
		unidCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			@SneakyThrows
			public void update(ViewerCell cell) {
				CursorEntry entry = (CursorEntry)cell.getElement();
				cell.setText(entry.getUnid());
			}
		});
		table.setSortColumn(unidCol.getColumn());
		table.setSortDirection(SWT.UP);
		unidCol.getColumn().addListener(SWT.Selection, event -> {
			TableColumn col = (TableColumn)event.widget;
			boolean ascending = true;
			if(table.getSortColumn() == col && table.getSortDirection() == SWT.UP) {
				ascending = false;
			}
			table.setSortColumn(col);
			table.setSortDirection(ascending ? SWT.UP : SWT.DOWN);
			resetDocList();
		});
		unidCol.getColumn().setWidth(250);

		
		createdCol = new TableViewerColumn(this.docs, SWT.NONE);
		createdCol.getColumn().setText("Created");
		createdCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			@SneakyThrows
			public void update(ViewerCell cell) {
				CursorEntry entry = (CursorEntry)cell.getElement();
				cell.setText(DATE_FORMAT.get().format(entry.getCreationDate()));
			}
		});
		createdCol.getColumn().addListener(SWT.Selection, event -> {
			TableColumn col = (TableColumn)event.widget;
			boolean ascending = true;
			if(table.getSortColumn() == col && table.getSortDirection() == SWT.UP) {
				ascending = false;
			}
			table.setSortColumn(col);
			table.setSortDirection(ascending ? SWT.UP : SWT.DOWN);
			resetDocList();
		});
		createdCol.getColumn().addListener(SWT.Selection, event -> {
			TableColumn col = (TableColumn)event.widget;
			table.setSortColumn(col);
		});
		createdCol.getColumn().setWidth(125);

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
		
		this.searchBox.addListener(SWT.KeyDown, event -> {
			if(event.keyCode == SWT.CR) {
				String query = this.searchBox.getText();
				this.searchQuery = query;
				resetDocList();
			}
		});
	}

	@SneakyThrows
	private void resetDocList() {
		Cursor cursor = store.openCursor();
		
		// Find a sort column if present
		TableColumn sortCol = docs.getTable().getSortColumn();
		if(sortCol != null) {
			String field = null;
			if(sortCol == unidCol.getColumn()) {
				field = "_unid";
			} else if(sortCol == createdCol.getColumn()) {
				field = "_cdate";
			}
			if(StringUtil.isNotEmpty(field)) {
				boolean asc = docs.getTable().getSortDirection() == SWT.UP;
				cursor.orderBy(field + (asc ? " a" : " d")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		
		if(StringUtil.isNotEmpty(this.searchQuery)) {
			try {
				JsonObject.fromJson(this.searchQuery);
				cursor.query(this.searchQuery);
			} catch (JsonException e) {
				// Then try it as an FT search
				cursor.ftSearch(this.searchQuery);
			}
		}
		
		this.docs.setContentProvider(new DocumentListContentProvider(this.docs, cursor));
	}
}
