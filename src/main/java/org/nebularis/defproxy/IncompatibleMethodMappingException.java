package org.nebularis.defproxy;

import org.nebularis.defproxy.support.MethodSignature;

/**
 * Created by IntelliJ IDEA.
 * User: carecx
 * Date: 29-Nov-2010
 * Time: 10:14:11
 * To change this template use File | Settings | File Templates.
 */
public class IncompatibleMethodMappingException extends MappingException {

    private final MethodSignature targetSig;

    public IncompatibleMethodMappingException(MethodSignature srcSig, MethodSignature targetSig) {
        super(srcSig);
        this.targetSig = targetSig;
    }
}
