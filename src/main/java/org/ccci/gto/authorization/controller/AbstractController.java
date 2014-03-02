package org.ccci.gto.authorization.controller;

import java.util.Collections;
import java.util.List;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.Commands;
import org.ccci.gto.authorization.Controller;
import org.ccci.gto.authorization.Processor;
import org.ccci.gto.authorization.Response;
import org.ccci.gto.authorization.exception.AlreadyProcessedException;
import org.ccci.gto.authorization.exception.NullCommandsException;
import org.ccci.gto.authorization.exception.ProcessingException;
import org.ccci.gto.authorization.object.Namespace;

public abstract class AbstractController implements Controller {
    private Processor processor;
    private Namespace baseNamespace = Namespace.ROOT;

    /**
     * @param processor
     *            the processor to set
     */
    public void setProcessor(final Processor processor) {
        this.processor = processor;
    }

    /**
     * @return the processor
     */
    public Processor getProcessor() {
        return this.processor;
    }

    /**
     * @return the baseNamespace
     */
    @Override
    public Namespace baseNamespace() {
        return baseNamespace;
    }

    public void setBaseNamespace(final String baseNamespace) {
        this.setBaseNamespace(new Namespace(baseNamespace));
    }

    /**
     * @param baseNamespace
     *            the baseNamespace to set
     */
    public void setBaseNamespace(final Namespace baseNamespace) {
        this.baseNamespace = baseNamespace;
    }

    @Override
    public final Commands newCommands() {
        return new Commands();
    }

    @Override
    public final Commands process(final Commands commands) throws ProcessingException {
        // throw an exception if a commands object isn't provided
        if (commands == null) {
            throw new NullCommandsException();
        }

        // throw an error if the commands have already been processed
        if (commands.isProcessed()) {
            throw new AlreadyProcessedException("The commands have already been processed");
        }

        // retrieve any pre/post commands
        final List<? extends Command> preCmds = this.preProcessCommands(commands.getCommands());
        final List<? extends Command> postCmds = this.postProcessCommands(commands.getCommands());

        // generate a commands object for all the commands being sent
        final Commands tmpCmds = new Commands();
        if (preCmds != null) {
            tmpCmds.addCommands(preCmds);
        }
        tmpCmds.addCommands(commands);
        if (postCmds != null) {
            tmpCmds.addCommands(postCmds);
        }

        // process the specified commands
        final List<Response<? extends Command>> responses = this.getProcessor().process(tmpCmds);
        
        // store the responses in the commands object (removing responses for
        // any pre/post commands)
        final int from = preCmds != null ? preCmds.size() : 0;
        final int to = responses.size() - (postCmds != null ? postCmds.size() : 0);
        commands.setResponses(responses.subList(from, to));

        // return the commands object to enable method chaining
        return commands;
    }

    protected List<? extends Command> preProcessCommands(final List<Command> commands) {
        return Collections.emptyList();
    }

    protected List<? extends Command> postProcessCommands(final List<Command> commands) {
        return Collections.emptyList();
    }
}
