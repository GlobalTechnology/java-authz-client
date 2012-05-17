package org.ccci.gto.authorization;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.ccci.gto.authorization.command.AddPermissions;
import org.ccci.gto.authorization.command.AddResources;
import org.ccci.gto.authorization.command.AddRoles;
import org.ccci.gto.authorization.command.AddToRoles;
import org.ccci.gto.authorization.command.Check;
import org.ccci.gto.authorization.command.DumpExecutionContext;
import org.ccci.gto.authorization.command.ListPermittedEntities;
import org.ccci.gto.authorization.command.Login;
import org.ccci.gto.authorization.command.RemoveResources;
import org.ccci.gto.authorization.command.RemoveRoles;
import org.ccci.gto.authorization.command.RestrictNamespaces;
import org.ccci.gto.authorization.exception.AlreadyProcessedException;
import org.ccci.gto.authorization.exception.InvalidCommandException;
import org.ccci.gto.authorization.exception.NotProcessedException;
import org.ccci.gto.authorization.exception.NullCommandException;
import org.ccci.gto.authorization.exception.ProcessingException;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Key;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Resource;
import org.ccci.gto.authorization.object.Role;
import org.ccci.gto.authorization.object.Target;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Commands {
    private final List<Command> commands = new ArrayList<Command>();
    private List<Response<? extends Command>> responses = null;

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

    /**
     * @return the commands
     */
    public List<Command> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    public Command getCommand(final int index) {
        return this.commands.get(index);
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

    /**
     * @return if this commands object has been processed
     */
    public boolean isProcessed() {
        return this.responses != null;
    }

    /**
     * @return the responses
     * @throws NotProcessedException
     */
    public List<Response<? extends Command>> getResponses() throws NotProcessedException {
        if (this.isProcessed()) {
            return this.responses;
        }

        throw new NotProcessedException();
    }

    public Response<? extends Command> getResponse(final int index) throws NotProcessedException {
        if (!this.isProcessed()) {
            throw new NotProcessedException();
        }

        return this.responses.get(index);
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

    /** Authorization Commands */

    public Commands addPermissions(final Collection<Entity> entities, final Collection<Target> targets)
            throws InvalidCommandException {
        try {
            // generate and add a new addPermissions command
            return this.addCommand(new AddPermissions(entities, targets));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addResources(final Collection<Resource> resources) throws InvalidCommandException {
        try {
            // generate and add a new addResources command
            return this.addCommand(new AddResources(resources));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addResources(final Resource... resources) throws InvalidCommandException {
        return this.addResources(Arrays.asList(resources));
    }

    public Commands addRoles(final Collection<Role> roles) throws InvalidCommandException {
        try {
            // generate and add a new addRoles command
            return this.addCommand(new AddRoles(roles));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addRoles(final Role... roles) throws InvalidCommandException {
        return this.addRoles(Arrays.asList(roles));
    }

    public Commands addToRoles(final Collection<Target> targets, final Collection<Role> roles)
            throws InvalidCommandException {
        try {
            // generate and add a new addToRoles command
            return this.addCommand(new AddToRoles(targets, roles));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands check(final Entity entity, final List<Target> targets) throws InvalidCommandException {
        try {
            // generate and add a new check command
            return this.addCommand(new Check(entity, targets));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands check(final Entity entity, final Target... targets) throws InvalidCommandException {
        return this.check(entity, Arrays.asList(targets));
    }

    public Commands dumpExecutionContext() throws InvalidCommandException {
        try {
            // generate and add a new dumpExecutionContext command
            return this.addCommand(new DumpExecutionContext());
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listPermittedEntities(final Collection<Target> targets) throws InvalidCommandException {
        try {
            // generate and add a new listPermittedEntities command
            return this.addCommand(new ListPermittedEntities(targets));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listPermittedEntities(final Collection<Target> targets, final Collection<Namespace> namespaces)
            throws InvalidCommandException {
        try {
            // generate and add a new listPermittedEntities command
            return this.addCommand(new ListPermittedEntities(targets, namespaces));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands login(final Key key) throws InvalidCommandException {
        try {
            // generate and add a new login command
            return this.addCommand(new Login(key));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removeResources(final Collection<Resource> resources) throws InvalidCommandException {
        try {
            // generate and add a new removeResources command
            return this.addCommand(new RemoveResources(resources));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removeResources(final Resource... resources) throws InvalidCommandException {
        return this.removeResources(Arrays.asList(resources));
    }

    public Commands removeRoles(final Collection<Role> roles) throws InvalidCommandException {
        try {
            // generate and add a new removeRoles command
            return this.addCommand(new RemoveRoles(roles));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removeRoles(final Role... roles) throws InvalidCommandException {
        return this.removeRoles(Arrays.asList(roles));
    }

    public Commands restrictNamespaces(final Collection<Namespace> namespaces) throws InvalidCommandException {
        try {
            // generate and add a new restrictNamespaces command
            return this.addCommand(new RestrictNamespaces(namespaces));
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands restrictNamespaces(final Namespace... namespaces) throws InvalidCommandException {
        return this.restrictNamespaces(Arrays.asList(namespaces));
    }
}
