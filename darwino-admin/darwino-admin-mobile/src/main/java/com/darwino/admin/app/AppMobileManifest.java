/*!COPYRIGHT HEADER! 
 *
 */

package com.darwino.admin.app;

import com.darwino.commons.json.JsonException;
import com.darwino.jsonstore.sql.impl.sqlite.SqliteDatabaseCustomizer;
import com.darwino.mobile.platform.DarwinoMobileManifest;
import com.darwino.mobile.platform.DarwinoMobileSettings;

/**
 * Mobile Application Manifest.
 */
public class AppMobileManifest extends DarwinoMobileManifest {
	
	public AppMobileManifest(String pathInfo) {
		super(pathInfo);
	}

	@Override
	public void initDefaultSettings(DarwinoMobileSettings settings) {
		super.initDefaultSettings(settings);
		// Set the default settings here
	}
	
	@Override
	public SqliteDatabaseCustomizer findDatabaseCustomizer(String dbName) throws JsonException {
		// Return your database customizer here
		return new AppDatabaseCustomizer();
	}

	@Override
	public String[] getReplicationInstances() {
		return AppDatabaseDef.getInstances();
	}
}
