package org.nebularis.defproxy.stubs;

import java.util.HashMap;
import java.util.Map;

public class StoredItem {

    private final Map<String, String> map = new HashMap<String, String>();

    public void set(final String name, final String value) {
        map.put(name, value);
    }

    public String get(final String name) {
        return map.get(name);
    }

    public String getProductId() {
        return get("productId");
    }

}
