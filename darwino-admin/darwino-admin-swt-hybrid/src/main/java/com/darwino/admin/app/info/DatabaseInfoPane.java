package com.darwino.admin.app.info;

import org.eclipse.swt.widgets.Composite;
import com.darwino.jsonstore.Database;

import lombok.SneakyThrows;

public class DatabaseInfoPane extends AbstractInfoPane {
	private final Database database;

	@SneakyThrows
	public DatabaseInfoPane(Composite parent, Database database) {
		super(parent, "Database");
		this.database = database;
		
		createChildren();
	}
	
	@SneakyThrows
	protected void createChildren() {
		info("Name:", database.getId());
		info("Documents:", database.documentCount());
		
	}

}
