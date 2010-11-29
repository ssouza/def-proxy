package org.nebularis.defproxy.utils;

/**
 * General purpose type conversion.
 */
public interface TypeConverter {

    /**
     * Convert object <i>o</i> to type T, which is
     * classified as <code>classHint</code>.
     * @param o
     * @param classHint
     * @param <T>
     * @return an object of type T
     * @throws 
     */
    <T> T convert(Object o, Class<T> classHint);

}
