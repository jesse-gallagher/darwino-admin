package com.darwino.admin.app.info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;

import com.darwino.jsonstore.Cursor;
import com.darwino.jsonstore.Store;
import com.darwino.jsonstore.callback.CursorEntry;

import lombok.SneakyThrows;

public class DocumentListContentProvider implements ILazyContentProvider {

	private final TableViewer tableViewer;
	private final Store store;
	private List<CursorEntry> entries;
	
	@SneakyThrows
	public DocumentListContentProvider(TableViewer tableViewer, Store store) {
		this.tableViewer = tableViewer;
		this.store = store;
		
		tableViewer.setItemCount(store.documentCount());
		
		// TODO make this actually lazy-load
		entries = new ArrayList<>(store.documentCount());
		store.openCursor()
			.options(Cursor.DATA_MODDATES)
			.find(entries::add);
	}

	@Override
	public void updateElement(int index) {
		tableViewer.replace(entries.get(index), index);
	}

}
