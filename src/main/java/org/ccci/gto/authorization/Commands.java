package org.ccci.gto.authorization;

import static org.ccci.gto.authorization.Constants.XMLNS_AUTHZ;

import org.ccci.gto.authorization.command.AddGroups;
import org.ccci.gto.authorization.command.AddPermissions;
import org.ccci.gto.authorization.command.AddResources;
import org.ccci.gto.authorization.command.AddRoles;
import org.ccci.gto.authorization.command.AddToGroups;
import org.ccci.gto.authorization.command.AddToRoles;
import org.ccci.gto.authorization.command.AddUsers;
import org.ccci.gto.authorization.command.Check;
import org.ccci.gto.authorization.command.DumpExecutionContext;
import org.ccci.gto.authorization.command.ListEntitiesWithAttributes;
import org.ccci.gto.authorization.command.ListEntityAttributes;
import org.ccci.gto.authorization.command.ListGroupMembers;
import org.ccci.gto.authorization.command.ListPermittedEntities;
import org.ccci.gto.authorization.command.Login;
import org.ccci.gto.authorization.command.RemoveFromGroups;
import org.ccci.gto.authorization.command.RemovePermissions;
import org.ccci.gto.authorization.command.RemoveResources;
import org.ccci.gto.authorization.command.RemoveRoles;
import org.ccci.gto.authorization.command.RestrictNamespaces;
import org.ccci.gto.authorization.exception.AlreadyProcessedException;
import org.ccci.gto.authorization.exception.InvalidCommandException;
import org.ccci.gto.authorization.exception.NotProcessedException;
import org.ccci.gto.authorization.exception.NullCommandException;
import org.ccci.gto.authorization.exception.ProcessingException;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class Commands implements Serializable {
    private static final long serialVersionUID = -589071576194917897L;

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

    public Commands addCommands(final List<? extends Command> cmds) throws AlreadyProcessedException {
        for (final Command cmd : cmds) {
            this.addCommand(cmd);
        }

        return this;
    }

    public Commands addCommands(final Commands cmds) throws AlreadyProcessedException {
        return this.addCommands(cmds.getCommands());
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

    public Commands addGroups(final Group... groups) throws AlreadyProcessedException, InvalidCommandException {
        return this.addGroups(Arrays.asList(groups));
    }

    public Commands addGroups(final Collection<? extends Group> groups) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            return this.addCommand(new AddGroups(groups));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addPermissions(final Collection<? extends Entity> entities,
            final Collection<? extends Target> targets) throws AlreadyProcessedException, InvalidCommandException {
        try {
            // generate and add a new addPermissions command
            return this.addCommand(new AddPermissions(entities, targets));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addResources(final Resource... resources) throws AlreadyProcessedException, InvalidCommandException {
        return this.addResources(Arrays.asList(resources));
    }

    public Commands addResources(final Collection<? extends Resource> resources) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new addResources command
            return this.addCommand(new AddResources(resources));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addRoles(final Role... roles) throws AlreadyProcessedException, InvalidCommandException {
        return this.addRoles(Arrays.asList(roles));
    }

    public Commands addRoles(final Collection<? extends Role> roles) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new addRoles command
            return this.addCommand(new AddRoles(roles));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addToGroups(final Collection<? extends Entity> entities, final Collection<Group> groups)
            throws AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new AddToGroups(entities, groups));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addToRoles(final Collection<? extends Target> targets, final Collection<Role> roles)
            throws AlreadyProcessedException, InvalidCommandException {
        try {
            // generate and add a new addToRoles command
            return this.addCommand(new AddToRoles(targets, roles));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addUsers(final User... users) throws AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new AddUsers(users));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands addUsers(final Collection<User> users) throws AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new AddUsers(users));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands check(final Entity entity, final Target... targets) throws AlreadyProcessedException,
            InvalidCommandException {
        return this.check(entity, Arrays.asList(targets));
    }

    public Commands check(final Entity entity, final List<? extends Target> targets) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new check command
            return this.addCommand(new Check(entity, targets));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands dumpExecutionContext() throws AlreadyProcessedException, InvalidCommandException {
        try {
            // generate and add a new dumpExecutionContext command
            return this.addCommand(new DumpExecutionContext());
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listEntitiesWithAttributes(final Attribute... attributes) throws AlreadyProcessedException,
            InvalidCommandException {
        return this.listEntitiesWithAttributes(Arrays.asList(attributes));
    }

    public Commands listEntitiesWithAttributes(final Collection<Attribute> attributes) throws
            InvalidCommandException, AlreadyProcessedException {
        return this.listEntitiesWithAttributes(attributes, Collections.singleton(Namespace.ROOT));
    }

    public Commands listEntitiesWithAttributes(final Collection<Attribute> attributes,
                                               final Collection<Namespace> namespaces) throws
            AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new ListEntitiesWithAttributes(attributes, namespaces));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listEntityAttributes(final Entity... entities) throws AlreadyProcessedException,
            InvalidCommandException {
        return this.listEntityAttributes(Arrays.asList(entities));
    }

    public Commands listEntityAttributes(final Collection<Entity> entities) throws InvalidCommandException,
            AlreadyProcessedException {
        return this.listEntityAttributes(entities, Collections.singleton(Namespace.ROOT));
    }

    public Commands listEntityAttributes(final Collection<Entity> entities, final Collection<Namespace> namespaces)
            throws AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new ListEntityAttributes(entities, namespaces));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listGroupMembers(final Group... groups) throws AlreadyProcessedException, InvalidCommandException {
        return this.listGroupMembers(Arrays.asList(groups));
    }

    public Commands listGroupMembers(final Collection<Group> groups) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            return this.addCommand(new ListGroupMembers(groups));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listGroupMembers(final Collection<Group> groups, final Collection<Namespace> namespaces)
            throws AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new ListGroupMembers(groups, namespaces));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listPermittedEntities(final Target... targets) throws AlreadyProcessedException,
            InvalidCommandException {
        return this.listPermittedEntities(Arrays.asList(targets));
    }

    public Commands listPermittedEntities(final Collection<? extends Target> targets) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new listPermittedEntities command
            return this.addCommand(new ListPermittedEntities(targets));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands listPermittedEntities(final Collection<? extends Target> targets,
            final Collection<Namespace> namespaces) throws AlreadyProcessedException, InvalidCommandException {
        try {
            // generate and add a new listPermittedEntities command
            return this.addCommand(new ListPermittedEntities(targets, namespaces));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands login(final Key key) throws AlreadyProcessedException, InvalidCommandException {
        try {
            // generate and add a new login command
            return this.addCommand(new Login(key));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removeFromGroups(final Collection<? extends Entity> entities, final Collection<Group> groups)
            throws AlreadyProcessedException, InvalidCommandException {
        try {
            return this.addCommand(new RemoveFromGroups(entities, groups));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removePermissions(final Collection<? extends Entity> entities,
            final Collection<? extends Target> targets) throws AlreadyProcessedException, InvalidCommandException {
        try {
            // generate and add a new removePermissions command
            return this.addCommand(new RemovePermissions(entities, targets));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removeResources(final Resource... resources) throws AlreadyProcessedException,
            InvalidCommandException {
        return this.removeResources(Arrays.asList(resources));
    }

    public Commands removeResources(final Collection<? extends Resource> resources) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new removeResources command
            return this.addCommand(new RemoveResources(resources));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands removeRoles(final Role... roles) throws AlreadyProcessedException, InvalidCommandException {
        return this.removeRoles(Arrays.asList(roles));
    }

    public Commands removeRoles(final Collection<? extends Role> roles) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new removeRoles command
            return this.addCommand(new RemoveRoles(roles));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }

    public Commands restrictNamespaces(final Namespace... namespaces) throws AlreadyProcessedException,
            InvalidCommandException {
        return this.restrictNamespaces(Arrays.asList(namespaces));
    }

    public Commands restrictNamespaces(final Collection<Namespace> namespaces) throws AlreadyProcessedException,
            InvalidCommandException {
        try {
            // generate and add a new restrictNamespaces command
            return this.addCommand(new RestrictNamespaces(namespaces));
        } catch (final AlreadyProcessedException propagated) {
            throw propagated;
        } catch (final Exception e) {
            throw new InvalidCommandException(e);
        }
    }
}
