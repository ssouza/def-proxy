package org.nebularis.defproxy.support;

import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.beanutils.MethodUtils.getMatchingAccessibleMethod;

public class MethodSignatureValidator {

    private static final Map<Class, Class> primativeBoxedRepresentations =
            new HashMap<Class, Class>() {{
                put(Integer.class, int.class);
                put(Long.class, long.class);
                put(Short.class, short.class);
                put(Double.class, double.class);
                put(Boolean.class, boolean.class);
                put(Character.class, char.class);
            }};

    private final Class<?> delegateClass;

    public MethodSignatureValidator(final Class<?> delegateClass) {
        this.delegateClass = delegateClass;
    }

    public boolean check(final String methodName) {
        // assume that the return type is void
        // assume that the input parameter type array is empty
        return check(void.class, methodName);
    }

    public boolean check(final String methodName, Class... inputTypes) {
        return check(void.class, methodName, inputTypes);
    }

    public boolean check(Class<?> returnType, final String methodName, final Class... inputTypes) {
        final Method method = getMatchingAccessibleMethod(delegateClass, methodName, inputTypes);
        return (method != null && isAssignable(returnType, method.getReturnType()));
    }

    private boolean isAssignable(final Class<?> expectedReturnType, final Class<?> methodReturnType) {
        final boolean straightCheck = ClassUtils.isAssignable(methodReturnType, expectedReturnType);
        if (!straightCheck) {
            if (methodReturnType.isPrimitive()) {
                try {
                    final Class metaType = (Class) expectedReturnType.getField("TYPE").get(expectedReturnType);
                    return ClassUtils.isAssignable(methodReturnType, metaType);
                } catch (Exception e) {
                    return false;
                } 
            }
        }
        return straightCheck;
    }

}
