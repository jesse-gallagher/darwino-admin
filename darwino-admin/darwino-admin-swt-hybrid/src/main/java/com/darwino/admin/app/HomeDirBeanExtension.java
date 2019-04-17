package com.darwino.admin.app;

import java.io.File;

import com.darwino.commons.Platform;
import com.darwino.commons.platform.beans.impl.AbstractJreXmlBeanExtension;
import com.darwino.commons.util.StringUtil;

public class HomeDirBeanExtension extends AbstractJreXmlBeanExtension {

	@Override
	protected boolean initFactories() {
		// Current user dir directory
		try {
			String h = System.getProperty("user.home") + File.separatorChar + ".darwino" + File.separatorChar; //$NON-NLS-1$ //$NON-NLS-2$
			if(StringUtil.isNotEmpty(h)) {
				File f = new File(h,"darwino-beans.xml"); //$NON-NLS-1$
				addFactoriesFromFile(f);
			}
		} catch(Exception ex) {
			Platform.log(ex);
		}
		return true;
	}
}