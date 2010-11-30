package org.nebularis.defproxy.configuration;

import org.nebularis.defproxy.configuration.MappingException;
import org.nebularis.defproxy.introspection.MethodSignature;

/**
 * Thrown when two methods have incompatible signatures and no
 * {@link org.nebularis.defproxy.utils.TypeConverter} has been specified.
 */
public class IncompatibleMethodMappingException extends MappingException {

    private final MethodSignature targetSig;

    public IncompatibleMethodMappingException(MethodSignature srcSig, MethodSignature targetSig) {
        super(srcSig);
        this.targetSig = targetSig;
    }
}
