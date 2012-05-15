package org.ccci.gto.authorization.response;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.ccci.gto.authorization.command.DumpExecutionContext;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.User;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public final class DumpExecutionContextResponse extends AbstractResponse<DumpExecutionContext> {
    private final User user;
    private final Collection<Namespace> namespaces;

    public DumpExecutionContextResponse(final DumpExecutionContext command, final Integer code, final User user,
            final Collection<Namespace> namespaces) {
        super(command, code);

        this.user = user;
        this.namespaces = Collections.unmodifiableSet(new HashSet<Namespace>(namespaces));
    }

    public DumpExecutionContextResponse(final DumpExecutionContext command, final Element commandXml,
            final XPath xpathEngine) throws InvalidXmlException {
        super(command, commandXml, xpathEngine);

        try {
            // extract the response node from the command
            final Element response = (Element) xpathEngine.evaluate("authz:response", commandXml, XPathConstants.NODE);

            // extract the user
            this.user = new User((Element) xpathEngine.evaluate("authz:user", response, XPathConstants.NODE));

            // extract the namespaces
            final NodeList nses = (NodeList) xpathEngine.evaluate("authz:namespaces/authz:namespace", response,
                    XPathConstants.NODESET);
            if (nses.getLength() > 0) {
                final HashSet<Namespace> namespaces = new HashSet<Namespace>(nses.getLength());
                for (int x = 0; x < nses.getLength(); x++) {
                    namespaces.add(new Namespace((Element) nses.item(x)));
                }
                this.namespaces = Collections.unmodifiableSet(namespaces);
            } else {
                this.namespaces = Collections.emptySet();
            }
        } catch (final Exception e) {
            throw new InvalidXmlException(e);
        }
    }

    public User getUser() {
        return this.user;
    }

    public Collection<Namespace> getNamespaces() {
        return this.namespaces;
    }
}
