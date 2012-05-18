package org.ccci.gto.authorization.controller;

import java.util.ArrayList;
import java.util.List;

import org.ccci.gto.authorization.Command;
import org.ccci.gto.authorization.command.Login;
import org.ccci.gto.authorization.object.Key;

public class DefaultController extends AbstractController {
    private Key loginKey = null;

    /**
     * @param key
     *            the key to use to automatically login with
     */
    public void setLoginKey(final Key key) {
        this.loginKey = key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.ccci.gto.authorization.controller.AbstractController#preProcessCommands
     * (java.util.List)
     */
    @Override
    protected List<? extends Command> preProcessCommands(final List<Command> commands) {

        // include the login command when requested
        if (loginKey != null) {
            boolean sendLogin = false;
            for (final Command cmd : commands) {
                final String type = cmd.type();
                if (type.equals("addUsers") || type.equals("changeUser") || type.equals("check")
                        || type.equals("restrictNamespaces") || type.equals("revokeLoginKeys")) {
                    // These commands don't require a login to work, check the
                    // next command
                    continue;
                } else if (type.equals("login")) {
                    // there is another login command, so we shouldn't send our own
                    // login command
                    break;
                } else {
                    // this command requires a login to work
                    sendLogin = true;
                }

                break;
            }

            // a login is required, prepend it to any inherited commands
            if (sendLogin) {
                final List<Command> preCmds = new ArrayList<Command>();
                preCmds.add(new Login(loginKey));
                preCmds.addAll(super.preProcessCommands(commands));
                return preCmds;
            }
        }

        // return the inherited pre-process commands
        return super.preProcessCommands(commands);
    }

}
