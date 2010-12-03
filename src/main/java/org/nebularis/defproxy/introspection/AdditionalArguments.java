package org.nebularis.defproxy.introspection;

import org.nebularis.defproxy.annotations.Insertion;

/**
* Created by IntelliJ IDEA.
* User: t4
* Date: Dec 3, 2010
* Time: 2:08:11 PM
* To change this template use File | Settings | File Templates.
*/
public /*NB: this should really be package local! */ class AdditionalArguments {
    public final Insertion insertion;
    public final Object[] params;

    public AdditionalArguments(final Insertion insertion, final Object[] params) {
        this.insertion = insertion;
        this.params = params;
    }
}
