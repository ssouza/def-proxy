package org.nebularis.defproxy;

import org.nebularis.defproxy.configuration.ProxyConfigurationBuilder;

/**
 * Basic interface for a proxy factory.
 */
public interface ProxyFactory {

    <T> T createProxy(final Object delegate, final Class<T> proxyInterface);

    <T> T createProxy(final Object delegate, final Class<T> proxyInterface, final ProxyConfigurationBuilder builder);
    
}
