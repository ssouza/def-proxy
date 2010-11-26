package org.nebularis.defproxy.support;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.stubs.Baz;
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.test.ObjectEqualityAndHashCodeVerifier;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(Theories.class)
public class MethodSignatureTestCase extends ObjectEqualityAndHashCodeVerifier<MethodSignature> {

    private static final Map<MethodSignature, Object> map = new HashMap<MethodSignature,Object>();

    @Theory
    public void methodSignatureCanBeUsedInHashTable(final MethodSignature sig) {
        assumeThat(sig, is(not(equalTo(null))));
        final Object check = new Object();
        map.put(sig, check);
        assertThat(map.get(sig), is(sameInstance(check)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void conversionFromMethodShouldThrowForNulls() {
        MethodSignature.fromMethod(null);
    }

    @Test
    public void conversionFromMethodShouldPopulateTheCorrectFields() throws NoSuchMethodException {
        final Method m = FooBar.class.getMethod("checkCompatibility", FooBar.class, String.class);
        final MethodSignature sig = MethodSignature.fromMethod(m);
        assertThat(sig.getName(), is(equalTo(m.getName())));
        assertThat(sig.getReturnType(), is(equalTo((Class)m.getReturnType())));
        assertThat(sig.getParameterTypes(), is(equalTo((Class[])m.getParameterTypes())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingEmptyReturnTypeWillThrow() {
        new MethodSignature(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingEmptyMethodNameWillThrow() {
        new MethodSignature(String.class, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingNullMethodNameWillThrow() {
        new MethodSignature(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingNullForAnyParameterTypeArrayMemberWillThrow() {
        new MethodSignature(String.class, "method1", String.class, null, Integer.class);
    }

    @Test
    public void methodSigCanBeUsedInHashTables() {

    }

    @DataPoint
    public static Object nil = null;

    @DataPoint
    public static Object o = new Object();

    @DataPoint
    public static MethodSignature retClass = new MethodSignature(Class.class, "foobar");

    @DataPoint
    public static MethodSignature retObj = new MethodSignature(Object.class, "foobar");

    @DataPoint
    public static MethodSignature retString = new MethodSignature(String.class, "foobar");

    @DataPoint
    public static MethodSignature fb1RetClass = new MethodSignature(Class.class, "foobar1");

    @DataPoint
    public static MethodSignature fb1RetObj = new MethodSignature(Object.class, "foobar1");

    @DataPoint
    public static MethodSignature fb1RetObjTakeObj = new MethodSignature(Object.class, "foobar1", Object.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeObjCopy1 = new MethodSignature(Object.class, "foobar1", Object.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeString = new MethodSignature(FooBar.class, "foobar1", String.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeStringCopy1 = new MethodSignature(Baz.class, "foobar1", String.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeString3 = new MethodSignature(Baz.class, "foobar3", Object.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeString3Copy1 = new MethodSignature(FooBar.class, "foobar3", String.class);

}
