package org.nebularis.defproxy.support;

/**
 * This annotation explicitly marks a method as delegated,
 * which is only useful when you've not annotated your interface
 * as {@link org.nebularis.defproxy.support.ProxyPassThrough}.
 */
public @interface ProxyDelegated {

    /**
     * Optional. Indicates the method name in the back-end to
     * which calls should be delegated. Where overloads are present,
     * the method's type signature will be used to determine the
     * correct callee.
     *
     * Example:
     * <pre>
     *      &#064;ProxyInterface(delegate=Baz.class)
     *      public interface Foo {
     *
     *          // this method will be resolved to a compatible signature in Baz.class
     *          &#064;ProxyDelegated
     *          String bar(final String name, final int age);
     *
     *          // this method will be resolved to a method `String flobby(void)` in Baz.class
     *          &#064;ProxyDelegated(methodName="flobby")
     *          String clobber();
     *      }
     * </pre>
     *
     * <em>This value is ignored if the {@link ProxyDelegated#methodInvocationHandler()}
     * option has been set. Otherwise, this value is required.</em>
     * @return
     */
    String methodName() default "";

    /**
     * Optional. Indicates a type which, once instantiated, will handle the
     * calls to the back-end.
     *
     * Example:
     * <pre>
     *      &#064;ProxyInterface(delegate=Baz.class)
     *      public interface Foo {
     *
     *          // this method will be resolved to a compatible signature in Baz.class
     *          &#064;ProxyDelegated(methodInvocationHandler=MyClass.class)
     *          String bar(final String name, final int age);
     *
     *      }
     * </pre>
     *
     * <em>This value is ignored if the {@link ProxyDelegated#methodName()}
     * option has been set. Otherwise, this value is required.</em>
     * @return
     */
    Class<? extends MethodInvoker> methodInvocationHandler();
}
