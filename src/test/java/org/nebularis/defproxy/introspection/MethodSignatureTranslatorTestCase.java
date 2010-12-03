package org.nebularis.defproxy.introspection;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.nebularis.defproxy.configuration.ProxyConfigurationBuilder;
import org.nebularis.defproxy.stubs.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

    @Test
    public void invalidDelegateMethodNamesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignatureTranslator translator =
                new MethodSignatureTranslator(interfaceMethod, MyProxyInterface.class, MyDelegate.class);

        translator.setDelegateMethodNameOverride("getFlobby");

        try {
            translator.verifyMethodSignatures();
            Assert.fail();
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(translator.getDelegateMethod())));
            assertThat(translator.getDelegateMethod().getName(), is(equalTo("getFlobby")));
            assertThat((Class<MyDelegate>)e.getTargetType(),
                    is(equalTo((Class<MyDelegate>)translator.getDelegateType())));
        }
    }

    @Test
    public void invalidDelegateReturnTypesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignatureTranslator translator =
                new MethodSignatureTranslator(interfaceMethod, MyProxyInterface.class, MyDelegate.class);

        // this mapping has a different return type!
        translator.setDelegateMethodNameOverride("getInteger");

        try {
            translator.verifyMethodSignatures();
            Assert.fail();
        } catch (IncompatibleMethodMappingException e) {
            assertThat(e.getSourceMethodSignature(), is(equalTo(translator.getInterfaceMethod())));
            assertThat(e.getTargetMethodSignature(), is(equalTo(translator.getDelegateMethod())));
        }
    }

    @Test
    public void invalidDelegateMethodSignaturesShouldThrow() throws MappingException {
        final MethodSignatureTranslator translator =
                new MethodSignatureTranslator(new MethodSignature(void.class, "method1"),
                        SimpleInterface.class, ComplexDelegate.class);
        try {
            translator.verifyMethodSignatures();
            Assert.fail();
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(translator.getDelegateMethod())));
            assertThat((Class<ComplexDelegate>)e.getTargetType(),
                    is(equalTo((Class<ComplexDelegate>)translator.getDelegateType())));
        }
    }

}
