package org.nebularis.defproxy.configuration;

import org.nebularis.defproxy.introspection.MethodSignature;

/**
 * Created by IntelliJ IDEA.
 * User: carecx
 * Date: 29-Nov-2010
 * Time: 13:30:54
 * To change this template use File | Settings | File Templates.
 */
public class MappingException extends Exception {
    protected final MethodSignature sig;

    public MappingException(final MethodSignature sig) {
        this.sig = sig;
    }
}
