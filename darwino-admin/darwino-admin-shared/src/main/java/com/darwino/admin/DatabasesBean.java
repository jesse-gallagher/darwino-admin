package com.darwino.admin;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.darwino.commons.Platform;
import com.darwino.commons.json.JsonException;
import com.darwino.commons.platform.ManagedBeansService;
import com.darwino.jsonstore.extensions.ExtensionRegistry;
import com.darwino.jsonstore.meta.DatabaseRuntimeChecker;
import com.darwino.jsonstore.sql.impl.full.JsonDb;
import com.darwino.jsonstore.sql.impl.full.LocalFullJsonDBServerImpl;
import com.darwino.jsonstore.sql.impl.full.SqlContext;
import com.darwino.platform.DarwinoManifest;

import lombok.SneakyThrows;

@ApplicationScoped
public class DatabasesBean {
	private final Map<JsonDb, LocalFullJsonDBServerImpl> beanServers = new IdentityHashMap<JsonDb, LocalFullJsonDBServerImpl>();
	
	public synchronized List<String> getJsonDbNames() {
		ManagedBeansService managedBeanService = Platform.getManagedBeansService();
		return Arrays.stream(managedBeanService.enumerate(JsonDb.BEAN_TYPE, false))
			.sorted()
			.collect(Collectors.toList());
	}
	
	public LocalFullJsonDBServerImpl getServerInstance(JsonDb jsonDb) throws JsonException, SQLException {
		return beanServers.computeIfAbsent(jsonDb, DatabasesBean::initLocalDBServer);
	}
	
	@SneakyThrows
	private static LocalFullJsonDBServerImpl initLocalDBServer(JsonDb jsonDb) {
		DarwinoManifest mf = new DarwinoManifest();
		
		SqlContext sqlContext = jsonDb.createSqlContext();
		DatabaseRuntimeChecker checker = mf.getDatabaseRuntimeChecker();
		LocalFullJsonDBServerImpl app = createLocalServer(sqlContext, checker);
		ExtensionRegistry extReg = mf.getExtensionRegistry();
		if(extReg!=null) {
			app.setExtensionRegistry(extReg);
		}
		return app; 
	}
	
	protected static LocalFullJsonDBServerImpl createLocalServer(SqlContext sqlContext,DatabaseRuntimeChecker databaseChecker) {
		LocalFullJsonDBServerImpl app = new LocalFullJsonDBServerImpl(sqlContext);
		app.setDatabaseVersion(databaseChecker);
		return app;
	}
}
