/*!COPYRIGHT HEADER! 
 *
 */

package com.darwino.admin.app;

import java.util.List;

import com.darwino.admin.Messages;
import com.darwino.commons.platform.beans.ManagedBeansExtension;

public class AppPlugin extends AppMobilePlugin {
	
	public AppPlugin() {
		super(Messages.getString("AppPlugin.appName")); //$NON-NLS-1$
	}
	
	@Override
	public void findExtensions(Class<?> serviceClass, List<Object> extensions) {
		super.findExtensions(serviceClass, extensions);
		
		if(serviceClass==ManagedBeansExtension.class) {
			extensions.add(new HomeDirBeanExtension());
		}
	}
}
