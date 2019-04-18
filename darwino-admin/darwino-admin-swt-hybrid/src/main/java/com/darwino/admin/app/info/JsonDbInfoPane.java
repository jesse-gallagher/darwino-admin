package com.darwino.admin.app.info;

import org.eclipse.swt.widgets.Composite;

import com.darwino.config.jsonstore.JsonDbJdbc;
import com.darwino.jsonstore.sql.impl.full.JsonDb;

import lombok.SneakyThrows;

public class JsonDbInfoPane extends AbstractInfoPane {

	private final JsonDb jsonDb;
	
	public JsonDbInfoPane(Composite parent, JsonDb jsonDb) {
		super(parent, "Server");
		this.jsonDb = jsonDb;
		
		createChildren();
	}
	
	@SneakyThrows
	protected void createChildren() {
		if(jsonDb instanceof JsonDbJdbc) {
			JsonDbJdbc jdbc = (JsonDbJdbc)jsonDb;
			info("Database type", jdbc.getDb());
			info("URL", jdbc.getUrl());
		}
	}

}
