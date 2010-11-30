package org.nebularis.defproxy.integration;

public interface ResourceInjectionProvider {

    Object lookupResource(final String resourceKey);

}
