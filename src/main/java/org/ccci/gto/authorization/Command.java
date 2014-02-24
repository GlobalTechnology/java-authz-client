package org.ccci.gto.authorization;

import java.util.Collection;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
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

public interface Command {
    public String type();

    public Collection<Namespace> getNamespaces();

    public Collection<Entity> getEntities();

    public Collection<Group> getGroups();

    public Collection<User> getUsers();

    public Collection<Target> getTargets();

    public Collection<Resource> getResources();

    public Collection<Role> getRoles();

    /**
     * This method only works for check authorization commands
     * 
     * @return the entity used in this command
     */
    public Entity getEntity();

    /**
     * This method only works for login authorization commands
     * 
     * @return the key used in this command
     */
    public Key getKey();

    public Response<? extends Command> newResponse(final Element commandXml, final XPath xpathEngine)
            throws InvalidXmlException;

    public Element toXml(final Document doc);
}
