/*
 * This file is part of TI. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of TI,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.ti.sink.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Factory for Log4j sinks.
 */
public final class Log4jSinks {

    /**
     * @return A consumer that uses Log4j
     */
    public static Consumer<String> log4j() {
        return log4j(Log4jSinks.class);
    }

    /**
     * @param loggerClass The class of the logger which writes into the sink.
     * @return A consumer that uses Log4j
     */
    public static Consumer<String> log4j(final Class<?> loggerClass) {
        return log4j(loggerClass, Logger::info);
    }

    /**
     * @param sink The sink to use.
     * @return A consumer that uses Log4j
     */
    public static Consumer<String> log4j(final BiConsumer<Logger, String> sink) {
        return log4j(Log4jSinks.class, sink);
    }

    /**
     * @param loggerClass The class of the logger which writes into the sink.
     * @param sink        The sink to use.
     * @return A consumer that uses Log4j
     */
    public static Consumer<String> log4j(final Class<?> loggerClass, final BiConsumer<Logger, String> sink) {
        return log4jSink(LogManager.getLogger(loggerClass), sink);
    }

    /**
     * @param logger The logger to use.
     * @param sink   The sink to use.
     * @return A consumer that uses Log4j
     */
    public static Consumer<String> log4jSink(final Logger logger, final BiConsumer<Logger, String> sink) {
        return message -> sink.accept(logger, message);
    }

}
