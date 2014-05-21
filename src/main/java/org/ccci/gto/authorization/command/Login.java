package org.ccci.gto.authorization.command;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.exception.NullKeyException;
import org.ccci.gto.authorization.object.Key;
import org.ccci.gto.authorization.response.GenericResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;

public final class Login extends AbstractCommand {
    private static final long serialVersionUID = -1714673175196910952L;

    private final static String TYPE = "login";

    private final Key key;

    public Login(final Key key) {
        this.key = key;

        // throw an error if an invalid key was specified
        if (this.key == null) {
            throw new NullKeyException();
        }
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Key getKey() {
        return this.key;
    }

    @Override
    public GenericResponse<Login> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException {
        return new GenericResponse<Login>(this, commandXml, xpathEngine);
    }

    @Override
    public Element toXml(final Document doc) {
        final Element cmdXml = super.toXml(doc);

        // attach the key to login with
        cmdXml.appendChild(this.key.toXml(doc));

        return cmdXml;
    }
}
