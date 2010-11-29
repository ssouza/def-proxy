package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.utils.TypeConverter;

public class IntOfStringConverter implements TypeConverter<Integer> {

    @Override
    public Integer convert(final Object o, final Class<Integer> classHint) {
        return Integer.parseInt(String.valueOf(o));
    }

}
