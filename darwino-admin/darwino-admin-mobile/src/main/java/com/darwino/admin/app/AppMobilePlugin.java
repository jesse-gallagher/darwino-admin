/*!COPYRIGHT HEADER! 
 *
 */

package com.darwino.admin.app;

import java.util.List;

import com.darwino.commons.httpclnt.SSLCertificateExtension;
import com.darwino.mobile.platform.MobileSSLCertificateExtension;
import com.darwino.commons.platform.impl.PluginImpl;



/**
 * Application Plugin.
 */
public abstract class AppMobilePlugin extends PluginImpl {
	
	public AppMobilePlugin(String name) {
		super(name);
	}

	@Override
	public void findExtensions(Class<?> serviceClass, List<Object> extensions) {
		AppBasePlugin.findExtensions(serviceClass, extensions);
		
		if(serviceClass==SSLCertificateExtension.class) {
			extensions.add(new MobileSSLCertificateExtension());
		}
	}
}
