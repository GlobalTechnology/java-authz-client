package org.ccci.gto.authorization;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import java.util.ArrayList;
import java.util.Collections;
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
    private final List<Command> commands = new ArrayList<Command>();
    private List<Response<? extends Command>> responses;

    public Commands addCommand(final Command cmd) throws AlreadyProcessedException {
        // throw an exception if a valid command isn't provided
        if (cmd == null) {
            throw new NullCommandException();
        }

        if (this.isProcessed()) {
            throw new AlreadyProcessedException("This commands object has already been processed");
        }

        // add the specified command to the list of commands to execute
        this.commands.add(cmd);

        // return this object to enable method chaining
        return this;
    }

    public Commands check(final Entity entity, final List<Target> targets) throws InvalidCommandException {
        // generate and add a new check command
        try {
            return this.addCommand(new Check(entity, targets));
        }
        // throw an invalid command exception
        catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands check(final Entity entity, final Target target) throws InvalidCommandException {
        // generate and add a new check command
        try {
            return this.addCommand(new Check(entity, target));
        }
        // throw an invalid command exception
        catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Command getCommand(final int index) {
        return this.commands.get(index);
    }

    /**
     * @return the commands
     */
    public List<Command> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    /**
     * @return the responses
     */
    public List<Response<? extends Command>> getResponses() {
        if (this.isProcessed()) {
            return this.responses;
        }

        return Collections.emptyList();
    }

    /**
     * @return if this commands object has been processed
     */
    public boolean isProcessed() {
        return this.responses != null;
    }

    /**
     * @param responses
     *            the responses to set
     * @throws ProcessingException
     */
    public void setResponses(final List<Response<? extends Command>> responses) throws ProcessingException {
        // throw an error if the current commands have already been processed
        if (this.isProcessed()) {
            throw new AlreadyProcessedException("attempted to store responses in an already processed commands object");
        }

        // throw an error if the incorrect number of responses are being set
        if (responses.size() != this.commands.size()) {
            // TODO: create a new exception class for this error
            throw new ProcessingException("attempting to store the incorrect number of responses");
        }

        // store the responses
        this.responses = Collections.unmodifiableList(new ArrayList<Response<? extends Command>>(responses));
    }

    public Element toXml(final Document doc) {
        // generate base xml node
        final Element commands = doc.createElementNS(XMLNS_AUTHZ, "commands");

        if (this.isProcessed()) {
        } else {
            // iterate over commands list appending xml for each command
            for (final Command cmd : this.commands) {
                commands.appendChild(cmd.toXml(doc));
            }
        }

        // return the generated authorization commands xml
        return commands;
    }
}
