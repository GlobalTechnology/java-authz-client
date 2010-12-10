package org.ccci.gcx.authorization;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class PdpFactory {
    private final Map<String,Pdp> pdps = new HashMap<String,Pdp>();

    public Pdp getPdp(final String uri) {
	//check for already existing PDP in pdps Map
	Pdp pdp = this.pdps.get(uri);

	//PDP doesn't exist yet, create it
	if(pdp == null) {
	    //create PDP object
	    pdp = new Pdp(uri);

	    //store PDP in pdps Map
	    this.pdps.put(uri, pdp);
	}

	//return the pdp for the specified uri
	return pdp;
    }
}
