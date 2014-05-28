package org.ccci.gto.authorization;

import org.ccci.gto.authorization.object.Attribute;
import org.ccci.gto.authorization.object.Entity;
import org.ccci.gto.authorization.object.Namespace;
import org.ccci.gto.authorization.object.Target;

import java.io.Serializable;
import java.util.Collection;

public interface Response<T extends Command> extends Serializable {
    public enum Code {
        OK(100),
        NO_ENTITIES(201), NO_USERS(202), NO_GROUPS(203), NO_TARGETS(204), NO_RESOURCES(205), NO_ROLES(206),
        NO_NAMESPACES(207), NO_KEYS(208), NO_ATTRIBUTES(209),
        INVALID_COMMAND(301), UNAUTHORIZED_COMMAND(302), INVALID_KEY(303),
        UNKNOWN(0);

        private final int code;

        Code(final int code) {
            this.code = code;
        }

        public static Code fromCode(final int code) {
            switch (code) {
                case 100:
                    return OK;
                case 201:
                    return NO_ENTITIES;
                case 202:
                    return NO_USERS;
                case 203:
                    return NO_GROUPS;
                case 204:
                    return NO_TARGETS;
                case 205:
                    return NO_RESOURCES;
                case 206:
                    return NO_ROLES;
                case 207:
                    return NO_NAMESPACES;
                case 208:
                    return NO_KEYS;
                case 209:
                    return NO_ATTRIBUTES;
                case 301:
                    return INVALID_COMMAND;
                case 302:
                    return UNAUTHORIZED_COMMAND;
                case 303:
                    return INVALID_KEY;
                default:
                    return UNKNOWN;
            }
        }

        public int getCode() {
            return this.code;
        }
    }

    public T getCommand();

    /**
     * @return the code
     */
    public Code getCode();

    public boolean isAuthorized(final int index);

    public boolean isAuthorized(final Target target);

    public boolean areAllAuthorized();

    public Collection<Namespace> getNamespaces();

    public Collection<Entity> getEntities();

    Collection<Attribute> getAttributes();
}
