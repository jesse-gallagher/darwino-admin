/*!COPYRIGHT HEADER! 
 *
 */

package com.darwino.admin.app.microservices;

import java.util.Date;

import com.darwino.commons.json.JsonException;
import com.darwino.commons.json.JsonObject;
import com.darwino.commons.microservices.JsonMicroService;
import com.darwino.commons.microservices.JsonMicroServiceContext;
import com.darwino.commons.util.DateFormatter;
import com.darwino.commons.util.StringUtil;
import com.darwino.jsonstore.Session;
import com.darwino.platform.DarwinoContext;



/**
 * Basic Hello World micro-service.
 */
public class HelloWorld implements JsonMicroService {
	
	public static final String NAME = "HelloWorld";
	
	@Override
	public void execute(JsonMicroServiceContext context) throws JsonException {
		Session session = DarwinoContext.get().getSession();
		JsonObject req = (JsonObject)context.getRequest();
		String greetings = req.getString("greetings"); 
		JsonObject result = JsonObject.of("message",StringUtil.format("Hello, {0}. {1}. It is {2} here, on the server!",
					session.getUser().getCn(),
					greetings,
					DateFormatter.getFormat("DEFAULT_TIME").format(new Date())));
		context.setResponse(result);
	}
}
