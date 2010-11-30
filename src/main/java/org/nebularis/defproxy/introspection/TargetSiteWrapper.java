package org.nebularis.defproxy.introspection;

import org.nebularis.defproxy.annotations.Insertion;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;

/**
 *
 */
public class TargetSiteWrapper extends MethodInvokerTemplate {
    private final Insertion insertion;
    private final Object[] additionalParams;

    public TargetSiteWrapper(final MethodSignature sig, final Insertion insertion, final Object... additionalParams) {
        super(sig);
        this.insertion = insertion;
        this.additionalParams = additionalParams;
    }

    @Override
    protected CallSite beforeInvocation(final Object delegate, final Method method, final Object[] params) {
        return new CallSite(method, delegate, mergeArgumentLists(params));
    }

    private Object[] mergeArgumentLists(final Object[] params) {
        final List<Object> addedParams = asList(additionalParams);
        final List<Object> suppliedParams = asList(params);
        if (insertion.equals(Insertion.Prefix)) {
            addedParams.addAll(suppliedParams);
            return addedParams.toArray();
        } else {
            suppliedParams.addAll(addedParams);
            return suppliedParams.toArray();
        }
    }
}
