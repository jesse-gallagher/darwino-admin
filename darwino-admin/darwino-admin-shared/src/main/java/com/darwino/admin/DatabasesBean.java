package com.darwino.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import com.darwino.commons.Platform;
import com.darwino.commons.platform.ManagedBeansService;
import com.darwino.jsonstore.sql.impl.full.JsonDb;

@ApplicationScoped
public class DatabasesBean {
	public synchronized List<String> getJsonDbNames() {
		ManagedBeansService managedBeanService = Platform.getManagedBeansService();
		return Arrays.stream(managedBeanService.enumerate(JsonDb.BEAN_TYPE, false))
			.sorted()
			.collect(Collectors.toList());
	}
}
