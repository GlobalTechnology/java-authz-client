package org.ccci.gcx.authorization;

import java.util.HashMap;
import java.util.Map;

public class PdpFactory {
	Map<String,Pdp> pdps;

	public PdpFactory() {
		super();
		this.pdps = new HashMap<String,Pdp>();
	}
	
	Pdp getPdp(String uri) {
		//check for already existing PDP in pdps Map
		Pdp pdp = pdps.get(uri);
		
		//PDP doesn't exist yet, create it
		if(pdp == null) {
			//create PDP object
			pdp = new Pdp();
			pdp.setGcxServerRoot(uri);
			
			//store PDP in pdps Map
			pdps.put(uri, pdp);
		}
		
		//return the pdp for the specified uri
		return pdp;
	}
}
