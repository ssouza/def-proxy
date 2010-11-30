package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.utils.TypeConverter;

public class IntOfStringConverter implements TypeConverter<String,Integer> {

    @Override
    public Class<? extends String> getInputType() {
        return String.class;
    }

    @Override
    public Class<? extends Integer> getOutputType() {
        return Integer.class;
    }

    @Override
    public Integer convert(final String o) {
        return Integer.parseInt(String.valueOf(o));
    }

}
