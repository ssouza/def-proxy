package org.nebularis.defproxy.introspection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Simple representation of a "dispatch-able" call site. Subclasses of
 * {@link org.nebularis.defproxy.introspection.MethodInvoker} can opt to
 * alter the default {@link org.nebularis.defproxy.introspection.CallSite}
 * which represents the target site to which a proxy invocation will be
 * dispatched at runtime.
 *
 * <pre>
 * public class FooMethodInvoker extends MethodInvokerTemplate {
 *
 *   &#064;Override
 *   protected CallSite beforeInvocation(final Object delegate, final Method method, final Object[] params) {
 *      // alter the formal parameter list.....
 *      return new CallSite(method, delegate, mergeParams(params));
 *   }
 *
 * }
 * </pre>
 */
public class CallSite {

    private final Object callee;
    private final Method target;
    private final Object[] arguments;

    public CallSite(final Method target, final Object callee, final Object[] arguments) {
        this.target = target;
        this.callee = callee;
        this.arguments = arguments;
    }

    public Object dispatch() throws InvocationTargetException, IllegalAccessException {
        return target.invoke(callee, arguments);
    }
}
