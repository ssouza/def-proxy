package org.nebularis.defproxy.configuration;

import org.nebularis.defproxy.introspection.MethodSignature;

/**
 * Base exception class, for unhandled conditions met during interface
 * to delegate method mapping.
 */
public class MappingException extends Exception {
    protected final MethodSignature sig;

    public MappingException(final MethodSignature sig) {
        this.sig = sig;
    }
}
