package org.nebularis.defproxy.annotations;

public class FooBarException extends RuntimeException {
    public FooBarException(final Exception ex) {
        super(ex);
    }
}
