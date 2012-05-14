package org.ccci.gto.authorization;

import java.util.List;

import org.ccci.gto.authorization.exception.AlreadyProcessedException;
import org.ccci.gto.authorization.exception.NullCommandsException;
import org.ccci.gto.authorization.exception.ProcessingException;

public class Controller {
    private Processor processor;

    /**
     * @return the processer
     */
    public Processor getProcessor() {
	return this.processor;
    }

    public Commands process(final Commands commands)
	    throws NullCommandsException, AlreadyProcessedException,
	    ProcessingException {
	// throw an exception if a commands object isn't provided
	if (commands == null) {
	    throw new NullCommandsException();
	}

	// throw an error if the commands have already been processed
	if (commands.isProcessed()) {
	    throw new AlreadyProcessedException(
		    "The commands have already been processed");
	}

	// process the specified commands
        final List<Response<? extends Command>> responses = this.getProcessor().process(commands);

	// store the responses in the commands object
	commands.setResponses(responses);

	// return the commands object to enable method chaining
	return commands;
    }

    /**
     * @param processer the processer to set
     */
    public void setProcesser(final Processor processor) {
	this.processor = processor;
    }
}
