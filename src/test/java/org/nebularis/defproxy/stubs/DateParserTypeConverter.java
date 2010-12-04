package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.introspection.TypeConverter;

import java.util.Date;

public class DateParserTypeConverter implements TypeConverter<String, Date> {
    @Override
    public Class<String> getInputType() {
        return String.class;
    }

    @Override
    public Class<Date> getOutputType() {
        return Date.class;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date convert(final String o) {
        return new Date(o);
    }
}
