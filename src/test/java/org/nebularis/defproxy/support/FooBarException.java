package org.nebularis.defproxy.support;

public class FooBarException extends RuntimeException {
    public FooBarException(final Exception ex) {
        super(ex);
    }
}
