package de.xn__ho_hia.interesting;

import java.util.ArrayList;
import java.util.HashMap;

import de.xn__ho_hia.interesting.builder.LoggerBuilder;

/**
 * Entry point for users.
 */
public class Interested {

    /**
     * @param logger
     *            The logger interface to proxy.
     * @return A logger builder for the given interface.
     */
    public static final <LOGGER> LoggerBuilder<LOGGER> in(final Class<LOGGER> logger) {
        return new LoggerBuilder<>(logger, new ArrayList<>(), new HashMap<>());
    }

}
