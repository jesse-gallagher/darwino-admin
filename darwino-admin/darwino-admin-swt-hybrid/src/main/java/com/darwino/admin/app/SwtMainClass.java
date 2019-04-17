/*!COPYRIGHT HEADER! 
 *
 */

package com.darwino.admin.app;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.darwino.admin.Messages;

/**
 * SWT Application
 */
public class SwtMainClass {
	public static final String APP_NAME = Messages.getString("appName"); //$NON-NLS-1$
	public static final String APP_ICON = "/logo-512.png"; //$NON-NLS-1$
	
	public static final ImageDescriptor IMAGE_SERVER;
	public static final ImageDescriptor IMAGE_DATABASE;
	public static final ImageDescriptor IMAGE_STORE;
	public static final ImageDescriptor IMAGE_STORE_LOCAL;
	static {
		IMAGE_SERVER = ImageDescriptor.createFromURL(SwtMainClass.class.getResource("/icons/network-server.png")); //$NON-NLS-1$
		IMAGE_DATABASE = ImageDescriptor.createFromURL(SwtMainClass.class.getResource("/icons/system-file-manager.png")); //$NON-NLS-1$
		IMAGE_STORE = ImageDescriptor.createFromURL(SwtMainClass.class.getResource("/icons/folder.png")); //$NON-NLS-1$
		IMAGE_STORE_LOCAL = ImageDescriptor.createFromURL(SwtMainClass.class.getResource("/icons/folder-visiting.png")); //$NON-NLS-1$
	}
	
	public static void main(String[] args) {
		try(SeContainer container = SeContainerInitializer.newInstance().initialize()) {

			System.setProperty("com.apple.mrj.application.apple.menu.about.name", APP_NAME); //$NON-NLS-1$
			Display display = Display.getDefault();
			Display.setAppName(APP_NAME);
			
			AppShell appShell = new AppShell(display);
			appShell.setText(APP_NAME);
			
			// Set the app icon
			try(InputStream is = SwtMainClass.class.getResourceAsStream(APP_ICON)) {
				Image icon = new Image(display, is);
				appShell.setImage(icon);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			appShell.open();
			appShell.layout();
			
			while(!appShell.isDisposed()) {
				if(!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}
	}
}
