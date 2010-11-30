package org.nebularis.defproxy.introspection;

import org.junit.Test;
import org.nebularis.defproxy.annotations.Insertion;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TargetSiteWrapperTestCase {

    @Test
    public void templateMethodOverridesCanModifyFormalParameterList() throws Throwable {
        final MethodSignature sig =
                MethodSignature.fromMethod(Map.class.getMethod("get", Object.class));
        final TargetSiteWrapper wrapper = new TargetSiteWrapper(sig, Insertion.Prefix, "foo");

        final Map<String, String> subject = new HashMap<String, String>() {{
            put("foo", "bar");
        }};
        
        final String value = (String) wrapper.handleInvocation(subject);
        assertThat(value, is(equalTo("bar")));
    }

}
