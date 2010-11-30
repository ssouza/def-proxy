package org.nebularis.defproxy.utils;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

public class ReflectionUtils {

    /**
     * Returns <code>true</code> if the methodReturnType is assignable from the expectedReturnType.
     * @param expectedReturnType
     * @param methodReturnType
     * @return
     */
    public static boolean isAssignable(final Class<?> expectedReturnType, final Class<?> methodReturnType) {
        if (expectedReturnType == null || methodReturnType == null) {
            return false;
        }
        final boolean straightCheck = ClassUtils.isAssignable(methodReturnType, expectedReturnType);
        if (!straightCheck) {
            if (methodReturnType.isPrimitive()) {
                return ClassUtils.isAssignable(methodReturnType, primitiveForOrSame(expectedReturnType));
            } else if (expectedReturnType.isPrimitive()) {
                return ClassUtils.isAssignable(expectedReturnType, primitiveForOrSame(methodReturnType));
            } 
        }
        return straightCheck;
    }

    /**
     * For each member of both class arrays, returns <code>true</code> if the
     * type from the first array is assignable from the type taken from the second array.
     * @param classArray
     * @param toClassArray
     * @return
     */
    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (ArrayUtils.isSameLength(classArray, toClassArray) == false) {
            return false;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (isAssignable(classArray[i], toClassArray[i]) == false) {
                return false;
            }
        }
        return true;
    }

    static <T> Class<? extends T> primitiveForOrSame(Class<T> clazz) {
        try {
            return (Class<? extends T>) clazz.getField("TYPE").get(clazz);
        } catch (Exception e) {
            return clazz;
        }
    }
}