/**
 * 
 */
package org.ccci.gcx.authorization;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;

/**
 * @author frett
 *
 */
public class PDP {

	private HttpClient client;

	/**
	 * 
	 */
	public PDP() {
		this.client = new HttpClient();
	}

	/**
	 * @param entity authorization entity to check authorization for
	 * @param target authorization target to check authorization for
	 * @return boolean indicating whether entity has access to target, defaults to false if an error is encountered
	 */
	public boolean check(String entity, String target) {

		//generate POST request
		String requestXML = "<?xml version='1.0' encoding='UTF-8'?><auth_question><entity name='" + entity + "'><target name='" + target + "'/></entity></auth_question>";
		PostMethod request = new PostMethod("http://dev.mygcx.org/authz");
		request.addParameter("auth_question", requestXML);

		//try executing the request
		try {
			this.client.executeMethod(request);
		}
		//we had an error while trying to execute the request
		catch (Exception e) {
			
		}
		finally {
			
		}
		// default to false
		return false;
	}
}
