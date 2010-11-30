package org.nebularis.defproxy.utils;

/**
 * General purpose type conversion.
 */
public interface TypeConverter<T1,T2> {

    /**
     * Gets the input type (from which you wish to convert).
     * @return
     */
    Class<? extends T1> getInputType();

    /**
     * Gets the output type (to which you wish to convert).
     * @return
     */
    Class<? extends T2> getOutputType();

    /**
     * Convert object <i>o</i> of type T1, to type T2.
     * @param o
     * @return an object of type T2
     * @throws 
     */
    T2 convert(T1 o);

}
