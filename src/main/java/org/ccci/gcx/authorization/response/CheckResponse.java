package org.ccci.gcx.authorization.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.ccci.gcx.authorization.Command;
import org.ccci.gcx.authorization.Response;
import org.ccci.gcx.authorization.command.Check;
import org.ccci.gcx.authorization.exception.InvalidXmlException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class CheckResponse extends Response {
    private final ArrayList<Boolean> responses;

    public CheckResponse(final Check command, final Element commandXml,
	    final XPath xpathEngine) throws InvalidXmlException {
	super(command, commandXml, xpathEngine);

	try {
	    // extract the entity and target XML nodes from the command XML
	    final Element entityXml = (Element) xpathEngine.evaluate(
		    "authz:entity | authz:user | authz:group", commandXml,
		    XPathConstants.NODE);
	    final NodeList targetsNL = (NodeList) xpathEngine.evaluate(
		    "authz:target | authz:resource | authz:role", entityXml,
		    XPathConstants.NODESET);

	    // generate the responses list
	    this.responses = new ArrayList<Boolean>(targetsNL.getLength());

	    // iterate over all found target xml nodes
	    for (int x = 0; x < targetsNL.getLength(); x++) {
		// extract the check response from the target xml
		final Element targetXml = (Element) targetsNL.item(x);
		this.responses.add(new Boolean(targetXml.getAttributeNS(null,
			"decision").equals("permit")));
	    }
	} catch (final Exception e) {
	    throw new InvalidXmlException(e);
	}

	// throw an error if there aren't enough responses
	if (this.responses.size() != command.getTargets().size()) {
	    throw new InvalidXmlException(
		    "Invalid number of check command responses");
	}
    }

    public CheckResponse(final Command command, final Integer code,
	    final List<Boolean> responses) {
	super(command, code);
	if (responses != null) {
	    this.responses = new ArrayList<Boolean>(responses);
	} else {
	    this.responses = new ArrayList<Boolean>(0);
	}
    }

    /**
     * @return the responses
     */
    public ArrayList<Boolean> getResponses() {
	return new ArrayList<Boolean>(this.responses);
    }
}
