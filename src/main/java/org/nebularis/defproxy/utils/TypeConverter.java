package org.nebularis.defproxy.utils;

/**
 * General purpose type conversion.
 */
public interface TypeConverter<T1,T2> {

    Class<? extends T1> getInputType();

    Class<? extends T2> getOutputType();

    /**
     * Convert object <i>o</i> to type T, which is
     * classified as <code>classHint</code>.
     * @param o
     * @return an object of type T
     * @throws 
     */
    T2 convert(T1 o);

}
