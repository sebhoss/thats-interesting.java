/*
 * This file is part of TI. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of TI,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.ti.sink.jul;

import java.lang.reflect.InvocationHandler;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

import static wtf.metio.ti.handler.StandardInvocationHandlers.stringFormat;

/**
 * Factory for java.util.logging based {@link InvocationHandler}s.
 */
public final class JULInvocationHandlers {

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerInfo() {
        return globalLogger(Logger::info);
    }

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerWarning() {
        return globalLogger(Logger::warning);
    }

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerFine() {
        return globalLogger(Logger::fine);
    }

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerConfig() {
        return globalLogger(Logger::config);
    }

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerFiner() {
        return globalLogger(Logger::finer);
    }

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerFinest() {
        return globalLogger(Logger::finest);
    }

    /**
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLoggerSevere() {
        return globalLogger(Logger::severe);
    }

    /**
     * @param sink The sink to use.
     * @return An invocation handler that prints method invocations to java.util.logging
     */
    public static InvocationHandler globalLogger(final BiConsumer<Logger, String> sink) {
        return stringFormat(JULSinks.globalLogger(sink));
    }

}
