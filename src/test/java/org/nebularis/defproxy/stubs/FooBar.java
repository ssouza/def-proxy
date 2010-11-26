package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.support.MethodSignatureValidatorTestCase;

/**
* Created by IntelliJ IDEA.
* User: t4
* Date: Nov 26, 2010
* Time: 12:54:53 PM
* To change this template use File | Settings | File Templates.
*/
public class FooBar {

    public void doSomething() {}
    public String getNameFor(final int mapping) {
        return null;
    }
    public Baz returnsSubClass() { return null; }
    public /*check boxed wrappers will work*/ boolean checkCompatibility(final /*check co-variant args */FooBar o) {
        return false;
    }

    public boolean checkCompatibility(final FooBar fb, final String s) {
        return false;
    }

}
