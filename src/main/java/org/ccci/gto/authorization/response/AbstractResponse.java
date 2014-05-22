package org.ccci.gto.authorization.response;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.exception.UnsupportedMethodException;
import org.ccci.gto.authorization.object.Attribute;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Target;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.Collection;

public abstract class AbstractResponse<T extends Command> implements Response<T> {
    private static final long serialVersionUID = -5437888889574490757L;

    private final T command;
    private final Integer code;

    public AbstractResponse(final T command, final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
	this.command = command;

	try {
	    // extract the response node from the command
	    final Element responseXml = (Element) xpathEngine.evaluate(
		    "authz:response", commandXml, XPathConstants.NODE);

	    // extract the response code
	    this.code = Integer.parseInt(responseXml.getAttributeNS(null, "code"));
	} catch(final Exception e) {
	    throw new InvalidXmlException(e);
	}
    }

    public AbstractResponse(final T command, final Integer code) {
        this.command = command;
        this.code = code;
    }

    /**
     * @return the code
     */
    @Override
    public Integer getCode() {
	return this.code;
    }

    @Override
    public T getCommand() {
	return this.command;
    }

    @Override
    public boolean isAuthorized(int index) {
        throw new UnsupportedMethodException("isAuthorized is not supported by " + this.getClass());
    }

    @Override
    public boolean isAuthorized(final Target target) {
        throw new UnsupportedMethodException("isAuthorized is not supported by " + this.getClass());
    }

    @Override
    public boolean areAllAuthorized() {
        throw new UnsupportedMethodException("areAllAuthorized is not supported by " + this.getClass());
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        throw new UnsupportedMethodException("getNamespaces is not supported by " + this.getClass());
    }

    @Override
    public Collection<Entity> getEntities() {
        throw new UnsupportedMethodException("getEntities is not supported by " + this.getClass());
    }

    @Override
    public Collection<Attribute> getAttributes() {
        throw new UnsupportedMethodException("getAttributes is not supported by " + this.getClass());
    }
}
