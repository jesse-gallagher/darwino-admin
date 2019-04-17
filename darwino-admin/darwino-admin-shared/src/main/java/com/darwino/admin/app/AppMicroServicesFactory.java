/*!COPYRIGHT HEADER! 
 *
 */

package com.darwino.admin.app;

import com.darwino.commons.microservices.StaticJsonMicroServicesFactory;

import com.darwino.admin.app.microservices.HelloWorld;


/**
 * Application Micro Services Factory.
 * 
 * This is the place to define custom application micro services.
 */
public class AppMicroServicesFactory extends StaticJsonMicroServicesFactory {
	
	public AppMicroServicesFactory() {
		add(HelloWorld.NAME, new HelloWorld());
	}
}
