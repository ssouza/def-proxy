package org.nebularis.defproxy;

import org.junit.Test;
import org.nebularis.defproxy.stubs.BadProxyInterface;
import org.nebularis.defproxy.stubs.MyDelegate;

public class HandlerConfigurationBuilderTestCase {

    final HandlerConfigurationBuilder builder = new HandlerConfigurationBuilder();

    @Test(expected=InvalidInterfaceMappingException.class)
    public void nonMatchingMethodSignaturesFailValidation() throws InvalidInterfaceMappingException {
        builder.addProxyInterface(BadProxyInterface.class);
        builder.addDelegate(MyDelegate.class);
        builder.generateHandlerConfiguration();
    }

    

    /*@Test
    public void byDefaultMethodsAreResolvedBasedOnCompleteSignature() {

        builder.addProxyInterface(MyProxyInterface.class);
        builder.addDelegate(MyDelegate.class);
        //noinspection unchecked
        assertThat((Class)builder.generateHandlerConfiguration().
                getDelegateForProxy(MyProxyInterface.class), is(equalTo((Class)MyDelegate.class)));

    }*/

}

