package org.nebularis.defproxy.annotations;

import org.nebularis.defproxy.utils.TypeConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a means for {@link org.nebularis.defproxy.annotations.ProxyDelegated}
 * decorated methods to return a different type than that of the mapped method
 * to which they delegate at runtime. This is achieved by associating a
 * {@link org.nebularis.defproxy.utils.TypeConverter} with the delegated method.
 * 
 * <pre>
 *      &#064;ProxyInterface(delegate = HashMap.class)
 *      public interface Item {
 *
 *          &#064;ProxyDelegated(methodName = "get")
 *          &#064;ProxyArguments(value = {"product-id"}, direction = Prefix)
 *          &#064;ProxyTypeConverter(provider = IntOfStringConverter.class)
 *          int productId();
 *      }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ProxyTypeConverter {
    /**
     * The provider class.
     * @return
     */
    Class<? extends TypeConverter> provider();
}
