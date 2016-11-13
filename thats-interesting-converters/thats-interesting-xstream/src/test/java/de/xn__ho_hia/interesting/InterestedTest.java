package de.xn__ho_hia.interesting;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import de.xn__ho_hia.interesting.converter.XStreamConverters;
import de.xn__ho_hia.interesting.handler.GenericInvocationHandler;

/**
 *
 *
 */
@RunWith(JUnitPlatform.class)
@SuppressWarnings("static-method")
public class InterestedTest {

    @SuppressWarnings("nls")
    @Test
    void shouldCreateNonNullProxyForInterface() {
        // given
        final TestInterface instance = new LoggerBuilder<>(TestInterface.class)
                .invocationHandler(new GenericInvocationHandler<>(
                        XStreamConverters.xml(),
                        System.out::println))
                .createLogger();

        // when
        instance.someMethod("test"); //$NON-NLS-1$

        final Pair pair = new Pair();
        pair.left = "one";
        pair.right = "two";
        instance.otherMethod(pair);

        instance.thirdMethod("test", pair);
        instance.thirdMethod("test", pair, 5);
        instance.thirdMethod("test", pair, 18, false);

        final Map<String, Integer> defaults = new HashMap<>();
        defaults.put("ains", Integer.valueOf(123));
        defaults.put("zwai", Integer.valueOf(759));
        defaults.put("draih", Integer.valueOf(634));
        instance.otherMethod(pair, defaults);

        // then
        Assertions.assertNotNull(instance);
    }

    static interface TestInterface {

        void someMethod(String someParam);

        void otherMethod(Pair pair);

        void otherMethod(Pair pair, Map<String, Integer> defaults);

        void thirdMethod(String someParam, Pair pair);

        void thirdMethod(String someParam, Pair pair, int num);

        void thirdMethod(String someParam, Pair pair, int num, boolean whatever);

    }

    static class Pair {

        public String left;
        public String right;

    }

}