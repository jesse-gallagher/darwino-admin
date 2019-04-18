package com.darwino.admin.app.info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;

import com.darwino.jsonstore.Cursor;
import com.darwino.jsonstore.callback.CursorEntry;

import lombok.SneakyThrows;

public class DocumentListContentProvider implements ILazyContentProvider {

	private final TableViewer tableViewer;
	private List<CursorEntry> entries;
	
	@SneakyThrows
	public DocumentListContentProvider(TableViewer tableViewer, Cursor cursor) {
		this.tableViewer = tableViewer;
		
		int count = cursor.count();
		tableViewer.setItemCount(count);
		
		// TODO make this actually lazy-load
		entries = new ArrayList<>(cursor.count());
		cursor
			.options(Cursor.DATA_MODDATES)
			.find(entries::add);
	}

	@Override
	public void updateElement(int index) {
		tableViewer.replace(entries.get(index), index);
	}

}
