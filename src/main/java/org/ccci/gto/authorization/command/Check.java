package org.ccci.gto.authorization.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPath;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.exception.InvalidXmlException;
import org.ccci.gto.authorization.exception.NullEntityException;
import org.ccci.gto.authorization.exception.NullObjectException;
import org.ccci.gto.authorization.exception.NullTargetException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Target;
import org.ccci.gto.authorization.response.CheckResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class Check extends Command {
    private final Entity entity;
    private final ArrayList<Target> targets;

    public Check(final Entity entity, final List<Target> targets)
	    throws NullObjectException {
	this.entity = entity;
	this.targets = new ArrayList<Target>(targets);

	// throw an error if an invalid entity was specified
	if (entity == null) {
	    throw new NullEntityException();
	}
	// throw an error if there are missing targets in the specified List
	final Iterator<Target> t = this.targets.iterator();
	while (t.hasNext()) {
	    if (t.next() == null) {
		throw new NullTargetException();
	    }
	}
    }

    public Check(final Entity entity, final Target target)
	    throws NullObjectException {
	this(entity, new ArrayList<Target>());
	this.targets.add(target);

	// throw an error if there is a target isn't specified
	if (target == null) {
	    throw new NullTargetException();
	}
    }

    /**
     * @return the entity
     */
    public Entity getEntity() {
	return this.entity;
    }

    /**
     * @return the targets
     */
    public List<Target> getTargets() {
	// create a new list to prevent action at a distance on the list of
	// targets being checked
	return new ArrayList<Target>(this.targets);
    }

    /* (non-Javadoc)
     * @see org.ccci.gcx.authorization.Command#newResponse(org.w3c.dom.Element, javax.xml.xpath.XPath)
     */
    @Override
    public CheckResponse newResponse(final Element commandXml,
	    final XPath xpathEngine) throws InvalidXmlException {
	return new CheckResponse(this, commandXml, xpathEngine);
    }

    @Override
    public Element toXml(final Document doc) {
	//generate base command
	final Element command = super.toXml(doc);
	command.setAttributeNS(null, "type", "check");

	//attach the entity for this check command
	final Element entity = this.entity.toXml(doc);
	command.appendChild(entity);

	// iterate over targets list appending xml for each Target
	final Iterator<Target> t = this.targets.iterator();
	while (t.hasNext()) {
	    entity.appendChild(t.next().toXml(doc));
	}

	// return the generated authorization command xml
	return command;
    }

}
