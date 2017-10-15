package wtf.metio.ti.converter;

import static org.eclipse.jdt.annotation.Checks.requireNonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Factory for standard converters.
 */
public final class StandardConverters {

    /** The standard field name that is going hold the class name of the logging interface. */
    public static final String CLASS               = "class";                                          //$NON-NLS-1$

    /** The standard field name that is going hold the method name of the logging interface. */
    public static final String METHOD              = "method";                                         //$NON-NLS-1$

    /** The standard field name that is going hold the arguments of the logging interface. */
    public static final String ARGUMENTS           = "arguments";                                      //$NON-NLS-1$

    /** The standard field name that is going hold the extras of the logging interface. */
    public static final String EXTRAS              = "extras";                                         //$NON-NLS-1$

    /** Default String format template. */
    @SuppressWarnings("nls")
    public static final String FORMAT_TEMPLATE     = CLASS + ": [%s] " + METHOD + ": [%s] " + ARGUMENTS
            + ": %s " + EXTRAS + ": %s";

    /** The default name/value template for String based converters. */
    public static final String NAME_VALUE_TEMPLATE = "%s: %s";                                         //$NON-NLS-1$

    /**
     * @return The configured method invocation converter using {@link #FORMAT_TEMPLATE and
     *         #standardArgumentsConverter()}.
     */
    public static final InvocationConverter<String> stringFormat() {
        return new StringFormatConverter(FORMAT_TEMPLATE, standardArgumentsConverter());
    }

    /**
     * @param template
     *            The format template to use.
     * @return The configured method invocation converter.
     */
    public static final InvocationConverter<String> stringFormat(final String template) {
        return new StringFormatConverter(template, standardArgumentsConverter());
    }

    /**
     * @param template
     *            The format template to use.
     * @param argumentConverter
     *            The argument converter to use.
     * @return The configured method invocation converter.
     */
    public static final InvocationConverter<String> stringFormat(
            final String template,
            final InvocationConverter<Object[]> argumentConverter) {
        return new StringFormatConverter(template, argumentConverter);
    }

    /**
     * @return Converter that matches {@link #FORMAT_TEMPLATE}
     */
    public static InvocationConverter<Object[]> standardArgumentsConverter() {
        return (proxy, method, args, extras) -> new Object[] {
                method.getDeclaringClass().getName(),
                method.getName(),
                combineNamesAndValues(requireNonNull(method.getParameters()), args),
                mapToString(extras)
        };
    }

    /**
     * @param method
     *            The invoked POI method.
     * @return The name of the POI.
     */
    public static String getPOIName(final Method method) {
        return requireNonNull(method.getDeclaringClass().getName());
    }

    /**
     * @param method
     *            The invoked POI method.
     * @return The name of the POI method.
     */
    public static String getPOIMethodName(final Method method) {
        return requireNonNull(method.getName());
    }

    private static String combineNamesAndValues(final Parameter[] parameters, final Object[] args) {
        final String[] namesAndValues = new String[args.length];
        for (int index = 0; index < args.length; index++) {
            namesAndValues[index] = formatNameAndValue(
                    requireNonNull(parameters[index].getName()), requireNonNull(args[index]));
        }
        return arrayToString(namesAndValues);
    }

    private static final String mapToString(final Map<String, Supplier<Object>> extras) {
        return streamToString(requireNonNull(extras.entrySet().stream()
                .map(entry -> formatNameAndValue(entry.getKey(), entry.getValue().get()))));
    }

    private static String formatNameAndValue(final String name, final Object value) {
        return requireNonNull(String.format(NAME_VALUE_TEMPLATE, name, value));
    }

    @SuppressWarnings("null")
    private static final String arrayToString(final String[] arguments) {
        return streamToString(Arrays.stream(arguments));
    }

    @SuppressWarnings({ "nls", "null" })
    private static final String streamToString(final Stream<String> stream) {
        return stream.collect(Collectors.joining(", ", "[", "]"));
    }

    private StandardConverters() {
        // factory class
    }

}