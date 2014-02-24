package org.ccci.gto.authorization.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.exception.NullEntityException;
import org.ccci.gto.authorization.exception.NullTargetException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.CheckResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Check extends AbstractCommand {
    public static final String TYPE = "check";
    private final Entity entity;
    private final List<Target> targets;

    public Check(final Entity entity, final Target... targets) {
        this(entity, Arrays.asList(targets));
    }

    public Check(final Entity entity, final List<? extends Target> targets) {
        this.entity = entity;
        this.targets = Collections.unmodifiableList(new ArrayList<Target>(targets));

        // throw an error if an invalid entity was specified
        if (this.entity == null) {
            throw new NullEntityException();
        }
        // throw an error if there are missing targets in the specified List
        for (final Target target : this.targets) {
            if (target == null) {
                throw new NullTargetException();
            }
        }

    }

    @Override
    public String type() {
        return TYPE;
    }

    /**
     * @return the entity
     */
    @Override
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * @return the targets
     */
    @Override
    public List<Target> getTargets() {
        return this.targets;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ccci.gcx.authorization.Command#newResponse(org.w3c.dom.Element,
     * javax.xml.xpath.XPath)
     */
    @Override
    public CheckResponse newResponse(final Element commandXml, final XPath xpathEngine) throws InvalidXmlException {
        return new CheckResponse(this, commandXml, xpathEngine);
    }

    @Override
    public Element toXml(final Document doc) {
        // generate base command
        final Element command = super.toXml(doc);

        // attach the entity for this check command
        final Element entity = this.entity.toXml(doc);
        command.appendChild(entity);

        // iterate over targets list appending xml for each Target
        for (final Target target : this.targets) {
            entity.appendChild(target.toXml(doc));
        }

        // return the generated authorization command xml
        return command;
    }
}
