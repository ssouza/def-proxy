package org.nebularis.defproxy;

import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.support.MethodSignature;

/**
 * Default, reflection based {@link org.nebularis.defproxy.support.MethodInvoker}.
 */
class DefaultMethodInvoker implements MethodInvoker {

    private final MethodSignature sig;

    public DefaultMethodInvoker(final MethodSignature sig) {
        this.sig = sig;
    }

    @Override
    public Object handleInvocation(final Object delegate, final Object[] objects) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
