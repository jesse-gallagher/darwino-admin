package com.darwino.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import com.darwino.commons.Platform;
import com.darwino.commons.json.JsonException;
import com.darwino.commons.platform.ManagedBeansService;
import com.darwino.jsonstore.Database;
import com.darwino.jsonstore.Session;
import com.darwino.jsonstore.extensions.ExtensionRegistry;
import com.darwino.jsonstore.meta.DatabaseRuntimeChecker;
import com.darwino.jsonstore.sql.impl.full.JsonDb;
import com.darwino.jsonstore.sql.impl.full.LocalFullJsonDBServerImpl;
import com.darwino.jsonstore.sql.impl.full.SqlContext;
import com.darwino.platform.DarwinoManifest;

import lombok.SneakyThrows;

@ApplicationScoped
public class DatabasesBean {
	private final Map<JsonDb, LocalFullJsonDBServerImpl> beanServers = new IdentityHashMap<>();
	private final Map<LocalFullJsonDBServerImpl, Session> serverSessions = new IdentityHashMap<>();
	private final Map<Session, Map<String, Database>> databases = new IdentityHashMap<>();
	
	public synchronized List<String> getJsonDbNames() {
		ManagedBeansService managedBeanService = Platform.getManagedBeansService();
		return Arrays.stream(managedBeanService.enumerate(JsonDb.BEAN_TYPE, false))
			.sorted()
			.collect(Collectors.toList());
	}
	
	public LocalFullJsonDBServerImpl getServerInstance(JsonDb jsonDb) throws JsonException, SQLException {
		return beanServers.computeIfAbsent(jsonDb, DatabasesBean::initLocalDBServer);
	}
	
	public Session getSession(LocalFullJsonDBServerImpl server, String instanceName) {
		return serverSessions.computeIfAbsent(server, key -> createSystemSession(key, instanceName));
	}
	
	public Database getDatabase(Session session, String databaseName) {
		Map<String, Database> dbMap = databases.computeIfAbsent(session, key -> new HashMap<>());
		return dbMap.computeIfAbsent(databaseName, key -> _getDatabase(session, key));
	}
	
	@PreDestroy
	public void closeSessions() {
		for(Session s : serverSessions.values()) {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	// *******************************************************************************
	// * Internal utility methods
	// *******************************************************************************
	
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
	
	@SneakyThrows
	private static Session createSystemSession(LocalFullJsonDBServerImpl server, String instanceName) {
		return server.createSystemSession(instanceName);
	}
	
	@SneakyThrows
	private static Database _getDatabase(Session session, String databaseName) {
		return session.getDatabase(databaseName);
	}
}
