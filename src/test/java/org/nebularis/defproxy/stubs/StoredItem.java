package org.nebularis.defproxy.stubs;

import java.util.HashMap;
import java.util.Map;

public class StoredItem {

    private final Map<String, Object> map = new HashMap<String, Object>();

    public void set(final String name, final Object value) {
        map.put(name, value);
    }

    public Object get(final String name) {
        return map.get(name);
    }

}
