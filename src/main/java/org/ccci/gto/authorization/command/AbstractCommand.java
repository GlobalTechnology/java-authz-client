package org.ccci.gto.authorization.command;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.exception.UnsupportedMethodException;
import org.ccci.gto.authorization.object.Attribute;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.object.Key;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Resource;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.object.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.xpath.XPath;
import java.util.Collection;

public abstract class AbstractCommand implements Command {
    @Override
    public abstract String type();

    @Override
    public Collection<Namespace> getNamespaces() {
        throw new UnsupportedMethodException("getNamespaces is not supported by " + this.getClass());
    }

    @Override
    public Collection<Entity> getEntities() {
        throw new UnsupportedMethodException("getEntities is not supported by " + this.getClass());
    }

    @Override
    public Collection<Group> getGroups() {
        throw new UnsupportedMethodException("getGroups is not supported by " + this.getClass());
    }

    @Override
    public Collection<User> getUsers() {
        throw new UnsupportedMethodException("getUsers is not supported by " + this.getClass());
    }

    @Override
    public Collection<Target> getTargets() {
        throw new UnsupportedMethodException("getTargets is not supported by " + this.getClass());
    }

    @Override
    public Collection<Resource> getResources() {
        throw new UnsupportedMethodException("getResources is not supported by " + this.getClass());
    }

    @Override
    public Collection<Role> getRoles() {
        throw new UnsupportedMethodException("getRoles is not supported by " + this.getClass());
    }

    @Override
    public Collection<Attribute> getAttributes() {
        throw new UnsupportedMethodException("getAttributes is not supported by " + this.getClass());
    }

    @Override
    public Entity getEntity() {
        throw new UnsupportedMethodException("getEntity is not supported by " + this.getClass());
    }

    @Override
    public Key getKey() {
        throw new UnsupportedMethodException("getKey is not supported by " + this.getClass());
    }

    @Override
    public Element toXml(final Document doc) {
        final Element cmdXml = doc.createElementNS(XMLNS_AUTHZ, "command");
        cmdXml.setAttributeNS(null, "type", this.type());
        return cmdXml;
    }

    @Override
    public abstract Response<? extends AbstractCommand> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException;
}
