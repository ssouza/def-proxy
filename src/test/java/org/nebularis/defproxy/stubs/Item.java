package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.annotations.*;

import static org.nebularis.defproxy.annotations.Insertion.*;

@ProxyInterface(delegate = StoredItem.class)
public interface Item {

    @ProxyDelegated(methodName = "get")
    @ProxyArguments(value = {"bar-code"}, direction = Prefix)
    String barcode();

    @ProxyDelegated(methodName = "get")
    @ProxyArguments(value = {"product-id"}, direction = Prefix)
    @ProxyTypeConverter(provider = IntOfStringConverter.class)
    int productId();

}
