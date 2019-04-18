package com.darwino.admin.app.info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;

import com.darwino.jsonstore.Cursor;
import com.darwino.jsonstore.callback.CursorEntry;

import lombok.SneakyThrows;

public class DocumentListContentProvider implements ILazyContentProvider {

	private static final int PAGE_SIZE = 100;
	
	private final TableViewer tableViewer;
	private List<CursorEntry> entries;
	private final Cursor cursor;
	
	@SneakyThrows
	public DocumentListContentProvider(TableViewer tableViewer, Cursor cursor) {
		this.tableViewer = tableViewer;
		this.cursor = cursor;
		
		int count = cursor.count();
		tableViewer.setItemCount(count);
		
		// TODO make this actually lazy-load
		entries = new ArrayList<>(cursor.count());
	}

	@Override
	public void updateElement(int index) {
		fetchPageTo(index);
		
		tableViewer.replace(entries.get(index), index);
	}

	@SneakyThrows
	private void fetchPageTo(int index) {
		int start = entries.size();
		if(index > start-1) {
			Cursor cursor = this.cursor.getStore().openCursor();
			cursor.fromJson(this.cursor.toJson());
			int end = index + PAGE_SIZE;
			cursor
				.options(Cursor.DATA_MODDATES)
				.range(start, end - start)
				.find(entries::add);
		}
	}
}
