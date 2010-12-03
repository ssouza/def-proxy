package org.nebularis.defproxy.introspection;

import org.junit.Assert;
import org.junit.Test;
import org.nebularis.defproxy.stubs.SimpleDelegate;
import org.nebularis.defproxy.stubs.SimpleInterface;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MethodSignatureTranslatorTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void showNotAllowNullInterfaceMethods() {
        new MethodSignatureTranslator(null, null, null);    
    }

    @Test(expected = IllegalArgumentException.class)
    public void showNotAllowNullInterfaceClasses() {
        new MethodSignatureTranslator(new MethodSignature(Object.class, "FOOBAR"), null, null);    
    }

    @Test(expected = IllegalArgumentException.class)
    public void showNotAllowNullDelegateClasses() {
        new MethodSignatureTranslator(new MethodSignature(String.class, "FOOBAR"), SimpleInterface.class, null);    
    }

    @Test
    public void invalidInterfaceMethodSignaturesShouldThrow() throws MappingException {
        final MethodSignatureTranslator translator =
                new MethodSignatureTranslator(new MethodSignature(String.class, "FOOBAR"),
                        SimpleInterface.class, SimpleDelegate.class);
        try {
            translator.verifyMethodSignatures();
            Assert.fail();
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(translator.getInterfaceMethod())));
            assertThat((Class<SimpleInterface>)e.getTargetType(),
                    is(equalTo((Class<SimpleInterface>)translator.getInterfaceType())));
        }
    }



}
