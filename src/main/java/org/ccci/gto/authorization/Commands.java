package org.ccci.gto.authorization;

import java.util.ArrayList;
import java.util.List;

import org.ccci.gto.authorization.command.Check;
import org.ccci.gto.authorization.exception.AlreadyProcessedException;
import org.ccci.gto.authorization.exception.InvalidCommandException;
import org.ccci.gto.authorization.exception.NullCommandException;
import org.ccci.gto.authorization.exception.ProcessingException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Target;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Commands {
    private boolean processed = false;
    private final ArrayList<Command> commands;
    private ArrayList<Response<? extends Command>> responses;

    public Commands() {
        this.commands = new ArrayList<Command>();
    }

    public Commands addCommand(final Command cmd) throws NullCommandException,
	    AlreadyProcessedException {
	// throw an exception if a valid command isn't provided
	if (cmd == null) {
	    throw new NullCommandException();
	}

	if (this.isProcessed()) {
	    throw new AlreadyProcessedException(
		    "This commands object has already been processed");
	}

	// add the specified command to the list of commands to execute
	this.commands.add(cmd);

	// return this object to enable method chaining
	return this;
    }

    public Commands check(final Entity entity, final List<Target> targets)
	    throws InvalidCommandException {
	// generate and add a new check command
	try {
	    this.addCommand(new Check(entity, targets));
	}
	// throw an invalid command exception
	catch (final Exception e) {
	    throw new InvalidCommandException(e);
	}

	// return the commands object to enable method chaining
	return this;
    }

    public Commands check(final Entity entity, final Target target)
	    throws InvalidCommandException {
	// generate and add a new check command
	try {
	    this.addCommand(new Check(entity, target));
	}
	// throw an invalid command exception
	catch (final Exception e) {
	    throw new InvalidCommandException(e);
	}

	// return the commands object to enable method chaining
	return this;
    }

    public Command getCommand(final int index) {
	return this.commands.get(index);
    }

    /**
     * @return the commands
     */
    public List<Command> getCommands() {
	// return a copy of the array list to prevent action at a distance
        return new ArrayList<Command>(this.commands);
    }

    /**
     * @return the responses
     */
    public List<Response<? extends Command>> getResponses() {
	if (!this.isProcessed()) {
	    return null;
	} else {
            return new ArrayList<Response<? extends Command>>(this.responses);
	}
    }

    /**
     * @return if this commands object has been processed
     */
    public boolean isProcessed() {
	return this.processed;
    }

    /**
     * @param responses
     *            the responses to set
     * @throws ProcessingException
     */
    public void setResponses(final List<Response<? extends Command>> responses)
	    throws ProcessingException {
	// throw an error if the current commands have already been processed
	if (this.responses != null || this.isProcessed()) {
	    throw new AlreadyProcessedException(
		    "attempted to store responses in an already processed commands object");
	}

	// throw an error if the incorrect number of responses are being set
	if (responses.size() != this.commands.size()) {
	    // TODO: create a new exception class for this error
	    throw new ProcessingException(
		    "attempting to store the incorrect number of responses");
	}

	// store the responses and mark the commands object as processed
        this.responses = new ArrayList<Response<? extends Command>>(responses);
	this.processed = true;
    }

    public Element toXml(final Document doc) {
	//generate base xml node
	final Element commands = doc.createElementNS(Constants.XMLNS_AUTHZ, "commands");

	// iterate over commands list appending xml for each command
        for (final Command cmd : this.commands) {
            commands.appendChild(cmd.toXml(doc));
	}

	// return the generated authorization commands xml
	return commands;
    }
}
