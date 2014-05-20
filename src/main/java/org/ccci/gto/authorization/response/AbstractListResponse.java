package org.ccci.gto.authorization.response;

import org.ccci.gto.authorization.AuthzObject;
import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.object.Attribute;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Group;
import org.ccci.gto.authorization.object.Key;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Resource;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.object.User;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractListResponse<T extends Command, O extends AuthzObject> extends AbstractResponse<T> {
    private final Class<O> objectClass;
    private final Collection<O> objects;

    public AbstractListResponse(final T command, final Integer code, final Class<O> objectClass,
            final Collection<O> objects) {
        super(command, code);
        this.objectClass = objectClass;
        if (objects == null || objects.size() == 0) {
            this.objects = Collections.emptyList();
        } else {
            this.objects = Collections.unmodifiableCollection(new ArrayList<>(objects));
        }
    }

    public AbstractListResponse(final T command, final Class<O> objectClass, final Element commandXml,
            final XPath xpathEngine) throws InvalidXmlException {
        super(command, commandXml, xpathEngine);
        this.objectClass = objectClass;

        try {
            // extract the response node from the command
            final Element responseXml = (Element) xpathEngine.evaluate("authz:response", commandXml,
                    XPathConstants.NODE);

            // extract any objects attached to the response
            final NodeList objectsNL = (NodeList) xpathEngine.evaluate("authz:entity | authz:user | authz:group | "
                    + "authz:target | authz:resource | authz:role | authz:namespace | authz:key", responseXml,
                    XPathConstants.NODESET);

            final ArrayList<O> objects = new ArrayList<O>(objectsNL.getLength());
            try {
                // iterate over all the found objects
                for (int x = 0; x < objectsNL.getLength(); x++) {
                    final Element object = (Element) objectsNL.item(x);

                    // switch on node type
                    switch (object.getLocalName()) {
                        case "entity":
                            objects.add(objectClass.cast(new Entity(object)));
                            break;
                        case "user":
                            objects.add(objectClass.cast(new User(object)));
                            break;
                        case "group":
                            objects.add(objectClass.cast(new Group(object)));
                            break;
                        case "target":
                            objects.add(objectClass.cast(new Target(object)));
                            break;
                        case "resource":
                            objects.add(objectClass.cast(new Resource(object)));
                            break;
                        case "role":
                            objects.add(objectClass.cast(new Role(object)));
                            break;
                        case "namespace":
                            objects.add(objectClass.cast(new Namespace(object)));
                            break;
                        case "key":
                            objects.add(objectClass.cast(new Key(object)));
                            break;
                        case "attribute":
                            objects.add(objectClass.cast(new Attribute(object)));
                            break;
                        default:
                            throw new InvalidXmlException("unrecognized object");
                    }
                }
            } catch (final ClassCastException e) {
                throw new InvalidXmlException("Unsupported object encountered", e);
            }

            this.objects = Collections.unmodifiableCollection(objects);
        } catch (final InvalidXmlException e) {
            throw e;
        } catch (final Exception e) {
            throw new InvalidXmlException(e);
        }
    }

    /**
     * @return the objects
     */
    public Collection<O> getObjects() {
        return this.objects;
    }

    @Override
    public Collection<Namespace> getNamespaces() {
        if (Namespace.class.equals(this.objectClass)) {
            @SuppressWarnings("unchecked")
            final Collection<Namespace> namespaces = (Collection<Namespace>) this.getObjects();
            return namespaces;
        }

        return super.getNamespaces();
    }

    @Override
    public Collection<Entity> getEntities() {
        if (Entity.class.equals(this.objectClass)) {
            @SuppressWarnings("unchecked")
            final Collection<Entity> entities = (Collection<Entity>) this.getObjects();
            return entities;
        }

        return super.getEntities();
    }
}
