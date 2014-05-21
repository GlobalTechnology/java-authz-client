package org.ccci.gto.authorization.response;

import org.ccci.gto.authorization.command.Check;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Target;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.Arrays;

public final class CheckResponse extends AbstractResponse<Check> {
    private static final long serialVersionUID = 7953290954064069883L;

    private final boolean[] responses;

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

            // throw an exception if there aren't the correct number of
            // responses
            final int targets = this.getCommand().getTargets().size();
            if (targetsNL.getLength() != targets) {
                throw new InvalidXmlException("invalid number of authorization responses returned");
            }

	    // generate the responses list
            this.responses = new boolean[targets];

            // iterate over all targets
            for (int x = 0; x < targets; x++) {
		// extract the check response from the target xml
		final Element targetXml = (Element) targetsNL.item(x);
                this.responses[x] = "permit".equals(targetXml.getAttributeNS(null, "decision"));
	    }
        } catch (final InvalidXmlException e) {
            throw e;
	} catch (final Exception e) {
	    throw new InvalidXmlException(e);
	}
    }

    public CheckResponse(final Check command, final Integer code, final boolean[] responses) {
	super(command, code);
        this.responses = Arrays.copyOf(responses, this.getCommand().getTargets().size());
    }

    @Override
    public boolean isAuthorized(int index) {
        if (index >= 0 && index < this.responses.length) {
            return this.responses[index];
        }

        return false;
    }

    @Override
    public boolean isAuthorized(final Target target) {
        return this.isAuthorized(this.getCommand().getTargets().indexOf(target));
    }

    @Override
    public boolean areAllAuthorized() {
        for (final boolean authorized : this.responses) {
            if (!authorized) {
                return false;
            }
        }

        return true;
    }
}
