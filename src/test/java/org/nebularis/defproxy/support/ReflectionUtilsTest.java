package org.nebularis.defproxy.support;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.nebularis.defproxy.support.ReflectionUtils.isAssignable;

/**
 * User: carecx
 * Date: 29-Nov-2010
 */
@RunWith(Theories.class)
public class ReflectionUtilsTest {
    private static final Class<Object> OBJ = Object.class;
    private static final Class<String> STR = String.class;

    // TODO convert to theories
    @Test
    public void testSameClassIsAssignable() {
        assertTrue(isAssignable(OBJ, OBJ));
        assertTrue(isAssignable(STR, STR));
    }

    @Test
    public void testCovarientClasses() {
        assertTrue(isAssignable(OBJ, STR));
        assertFalse(isAssignable(STR, OBJ));
    }

    @Test
    public void testPrimitiveWrappersAreAssignable() {
        assertTrue(isAssignable(Integer.class, Integer.TYPE));
        assertTrue(isAssignable(Integer.TYPE, Integer.class));
        assertTrue(isAssignable(Integer.TYPE, Integer.TYPE));
    }
    
}
