package org.nebularis.defproxy.introspection;

import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.stubs.StoredItem;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;
import org.nebularis.defproxy.utils.TypeConverter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

// and all because you can't run with JMock.class and Theories.class :(
@RunWith(JMock.class)
public class MethodSignatureMockTestCase extends AbstractJMockTestSupport {

    @Test
    public void methodSigUsesTypeConverterWhenSupplied() throws Throwable {
        final TypeConverter converter = mock(TypeConverter.class);
        one(converter).convert(with("59"));
        will(returnValue(59));
        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature delegateMethod = new MethodSignature(String.class, "getProductId");
        final MethodInvoker invoker = new MethodInvokerTemplate(delegateMethod);
        invoker.setTypeConverter(converter);

        assertThat((Integer) invoker.handleInvocation(item), is(equalTo(59)));
    }

}
