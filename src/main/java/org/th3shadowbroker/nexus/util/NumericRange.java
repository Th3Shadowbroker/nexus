package org.th3shadowbroker.nexus.util;

import lombok.Data;
import org.th3shadowbroker.nexus.exceptions.RangeFormatException;

import java.util.Random;

/**
 * Class for handling ranges like '5-10'.
 */
@Data
public class NumericRange {

    /**
     * The minimum.
     */
    private final double min;

    /**
     * The maximum.
     */
    private final double max;

    /**
     * A random for generating randoms within the range.
     */
    private final Random random;

    /**
     * Create a new numeric range.
     * The order of the passed integers doesn't matter.
     * @param a The first value.
     * @param b The second value.
     */
    public NumericRange(double a, double b) {
        this.min = Math.min(a, b);
        this.max = Math.max(a, b);
        this.random = new Random();
    }

    /**
     * Get an integer within the range.
     * @return A random integer.
     */
    public int getIntWithin() {
        if (!isActualRange()) return getMinInt();
        return getMinInt() + random.nextInt(getMaxInt() + 1 - getMinInt());
    }

    /**
     * Get a double within the range.
     * @return A random double.
     */
    public double getDoubleWithing() {
        if (!isActualRange()) return max;
        return min + (max - min) * random.nextDouble();
    }

    /**
     * True if min isn't max.
     * @return True if min isn't max.
     */
    public boolean isActualRange() {
        return min != max;
    }

    /**
     * Get the minimum as an integer.
     * @return The minimum.
     */
    public int getMinInt() {
        return (int) min;
    }

    /**
     * Get the maximum as an integer.
     * @return The maximum.
     */
    public int getMaxInt() {
        return (int) max;
    }

    @Override
    public String toString() {
        return toString('-');
    }

    public String toString(char separator) {
        return String.format("%s%s%s", min, separator, max);
    }

    /**
     * Parse the given string.
     * @param value The string.
     * @return The parsed range.
     * @throws RangeFormatException If the value couldn't be parsed.
     */
    public static NumericRange parse(String value) throws RangeFormatException {
        return parse(value, '-');
    }

    /**
     * Parse the given string.
     * @param value The string.
     * @param separator The separator.
     * @return The parsed range.
     * @throws RangeFormatException If the value couldn't be parsed.
     */
    public  static NumericRange parse(String value, char separator) throws RangeFormatException {
        String[] splitted = value.split(String.valueOf(separator));
        if (splitted.length == 2) {
            try {

                // Parse values
                double a = Double.parseDouble(splitted[0]);
                double b = Double.parseDouble(splitted[1]);

                return new NumericRange(a, b);

            // a or b is not a valid double
            } catch (NumberFormatException ex) {
                throw new RangeFormatException(value, ex);
            }
        }

        // Invalid input
        throw new RangeFormatException(value, null);
    }

}
