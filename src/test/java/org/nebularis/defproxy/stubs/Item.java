package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.support.*;

import java.util.Date;
import static org.nebularis.defproxy.support.ProxyArguments.Direction.*;

@ProxyInterface(delegate = StoredItem.class)
public interface Item {

    @ProxyDelegated(methodName = "get")
    @ProxyArguments(value = {"bar-code"}, direction = Prefix)
    String barcode();

    @ProxyDelegated(methodName = "get")
    @ProxyArguments(value = {"bar-code"}, direction = Prefix)
    @ProxyTypeConverter(provider = TypeConverter.class)
    int productId();

    Date displayUntil();

}
