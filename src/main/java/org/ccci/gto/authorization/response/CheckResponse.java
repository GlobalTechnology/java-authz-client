package org.ccci.gto.authorization.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.command.Check;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class CheckResponse extends Response {
    private final List<Boolean> responses;

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
            final ArrayList<Boolean> responses = new ArrayList<Boolean>(targetsNL.getLength());

	    // iterate over all found target xml nodes
	    for (int x = 0; x < targetsNL.getLength(); x++) {
		// extract the check response from the target xml
		final Element targetXml = (Element) targetsNL.item(x);
                responses.add(new Boolean(targetXml.getAttributeNS(null, "decision").equals("permit")));
	    }

            this.responses = Collections.unmodifiableList(responses);
	} catch (final Exception e) {
	    throw new InvalidXmlException(e);
	}

	// throw an error if there aren't enough responses
	if (this.responses.size() != command.getTargets().size()) {
            throw new InvalidXmlException("Invalid number of check command responses");
	}
    }

    public CheckResponse(final Command command, final Integer code, final List<Boolean> responses) {
	super(command, code);
	if (responses != null) {
            this.responses = Collections.unmodifiableList(new ArrayList<Boolean>(responses));
	} else {
            this.responses = Collections.emptyList();
	}
    }

    /**
     * @return the responses
     */
    public List<Boolean> getResponses() {
        return this.responses;
    }
}
