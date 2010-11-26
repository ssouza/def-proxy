package org.nebularis.defproxy.support;

/**
 * When this annotates a target interface, all members
 * are passed through to the underlying delegate with
 * a direct mapping of the method signature. Co-variant
 * method return types <i>are</i> supported.
 *
 * Setting {@link org.nebularis.defproxy.support.ProxyPassThrough}
 * at the type level is the equivalent of annotating all the
 * interface's methods with {@link org.nebularis.defproxy.support.ProxyDelegated}.
 */
public @interface ProxyPassThrough {}
