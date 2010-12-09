package org.ccci.gcx.authorization;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class Command {
    private final String id;

    public Command() {
	this.id = Integer.toString(this.hashCode());
    }

    public Command(final String id) {
	this.id = id;
    }

    /**
     * @return the id for the current command object
     */
    public String getId() {
	return this.id;
    }

    public Element toXml(final Document doc) {
	final Element cmd = doc
		.createElementNS(AuthzConstants.XMLNS, "command");
	cmd.setAttributeNS(null, "id", this.id);
	return cmd;
    }
}
