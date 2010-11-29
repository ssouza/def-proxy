package org.nebularis.defproxy.support;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

public class ReflectionUtils {
    public ReflectionUtils() {
    }

    public static boolean isAssignable(final Class<?> expectedReturnType, final Class<?> methodReturnType) {
        final boolean straightCheck = ClassUtils.isAssignable(methodReturnType, expectedReturnType);
        if (!straightCheck) {
            if (methodReturnType.isPrimitive()) {
                try {
                    final Class metaType = (Class) expectedReturnType.getField("TYPE").get(expectedReturnType);
                    return isAssignable(methodReturnType, metaType);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return straightCheck;
    }

    public static boolean isAssignable(Class[] classArray, Class[] toClassArray) {
        if (ArrayUtils.isSameLength(classArray, toClassArray) == false) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; i++) {
            if (isAssignable(classArray[i], toClassArray[i]) == false) {
                return false;
            }
        }
        return true;
    }
}